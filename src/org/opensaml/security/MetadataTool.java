/*
 * Copyright [2006] [University Corporation for Advanced Internet Development, Inc.]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensaml.security;

import jargs.gnu.CmdLineParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.opensaml.Configuration;
import org.opensaml.common.SignableSAMLObject;
import org.opensaml.common.impl.SAMLObjectContentReference;
import org.opensaml.common.xml.ParserPoolManager;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.parse.XMLParserException;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureBuilder;
import org.opensaml.xml.signature.SignatureValidator;
import org.opensaml.xml.signature.Signer;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.util.XMLHelper;
import org.opensaml.xml.validation.ValidationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A tool for retrieving, verifying, and signing metadata.
 */
public class MetadataTool {

    /** Logger */
    private static Logger log;

    /**
     * Main entry point to program.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Configuration.init();
        configureLogging();

        CmdLineParser parser = CLIParserBuilder.buildParser();

        try {
            parser.parse(args);
        } catch (CmdLineParser.OptionException e) {
            errorAndExit(e.getMessage(), e);
        }

        Boolean helpEnabled = (Boolean) parser.getOptionValue(CLIParserBuilder.HELP_ARG);
        if (helpEnabled != null) {
            printHelp(System.out);
            System.out.flush();
            System.exit(0);
        }

        Boolean verify = (Boolean) parser.getOptionValue(CLIParserBuilder.VALIDATE_ARG);
        String inputFile = (String) parser.getOptionValue(CLIParserBuilder.INPUT_FILE_ARG);
        SignableSAMLObject metadata = (SignableSAMLObject) fetchMetadata(inputFile, verify);

        String keystorePath = (String) parser.getOptionValue(CLIParserBuilder.KEYSTORE_ARG);
        String storeType = (String) parser.getOptionValue(CLIParserBuilder.KEYSTORE_TYPE_ARG);
        String storePass = (String) parser.getOptionValue(CLIParserBuilder.KEYSTORE_PASS_ARG);
        String alias = (String) parser.getOptionValue(CLIParserBuilder.ALIAS_ARG);
        String keyPass = (String) parser.getOptionValue(CLIParserBuilder.KEY_PASS_ARG);
        Boolean sign = (Boolean) parser.getOptionValue(CLIParserBuilder.SIGN_ARG);
        if (sign != null && sign.booleanValue()) {
            KeyStore keystore = getKeyStore(keystorePath, storeType, storePass);
            PrivateKey signingKey = getPrivateKey(keystore, alias, keyPass);
            sign(metadata, signingKey);
        } else {
            if (keystorePath != null) {
                KeyStore keystore = getKeyStore(keystorePath, storeType, storePass);
                PublicKey verificationKey = getPublicKey(keystore, alias);
                verifySiganture(metadata, verificationKey);
            }
        }

        String outputFile = (String) parser.getOptionValue(CLIParserBuilder.OUTPUT_FILE_ARG);
        printMetadata(metadata, outputFile);
    }

    /**
     * Fetches metadata from either the given input file or URL.
     * 
     * @param inputFile the filesystem path to the metadata file.
     * @param inputURL the URL to the metadata file
     * 
     * @return the metadata
     */
    private static XMLObject fetchMetadata(String inputFile, Boolean validate) {
        if (DatatypeHelper.isEmpty(inputFile)) {
            errorAndExit("No input file was specified.", null);
        }

        try {
            log.debug("Fetching metadata from input " + inputFile);
            URL inputURL = new URL(inputFile);
            ParserPoolManager parser = ParserPoolManager.getInstance();
            Document metadatDocument = parser.parse(inputURL.openStream());

            if (validate != null && validate.booleanValue()) {
                parser.validate(metadatDocument);
                log.info("Metadata document passed validation");
            }

            Element metadataRoot = metadatDocument.getDocumentElement();
            Unmarshaller unmarshaller = Configuration.getUnmarshallerFactory().getUnmarshaller(metadataRoot);
            return unmarshaller.unmarshall(metadataRoot);
        } catch (MalformedURLException e) {
            errorAndExit("Input file/url was not properly formed", e);
        } catch (XMLParserException e) {
            errorAndExit("Unable to parse and validate metadata document", e);
        } catch (IOException e) {
            errorAndExit("Unable to read input file/url", e);
        } catch (UnmarshallingException e) {
            errorAndExit("Unable to unmarshall metadata", e);
        }

        return null;
    }

    /**
     * Gets the Java keystore.
     * 
     * @param keyStore path to the keystore
     * @param storeType keystore type
     * @param storePass keystore password
     * 
     * @return the keystore
     */
    private static KeyStore getKeyStore(String keyStore, String storeType, String storePass) {
        try {
            FileInputStream keyStoreIn = new FileInputStream(keyStore);
            KeyStore ks = KeyStore.getInstance(storeType);

            storePass = DatatypeHelper.safeTrimOrNullString(storePass);
            if (storePass != null) {
                ks.load(keyStoreIn, storePass.toCharArray());
                return ks;
            } else {
                log.error("No password provided for keystore");
                System.exit(1);
            }
        } catch (Exception e) {
            log.error("Unable to load keystore from file " + keyStore, e);
            System.exit(1);
        }

        return null;
    }

    /**
     * Gets the a private key from the keystore.
     * 
     * @param keystore keystore to fetch the key from
     * @param alias the key alias
     * @param keyPass password for the key
     * 
     * @return the private key or null
     */
    private static PrivateKey getPrivateKey(KeyStore keystore, String alias, String keyPass) {
        alias = DatatypeHelper.safeTrimOrNullString(alias);
        if (alias == null) {
            log.error("Key alias may not be null or empty");
            System.exit(1);
        }

        keyPass = DatatypeHelper.safeTrimOrNullString(keyPass);
        if (keyPass == null) {
            log.error("Private key password may not be null or empty");
            System.exit(1);
        }
        KeyStore.PasswordProtection keyPassParam = new KeyStore.PasswordProtection(keyPass.toCharArray());
        try {
            KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry) keystore.getEntry(alias, keyPassParam);
            return pkEntry.getPrivateKey();
        } catch (Exception e) {
            log.error("Unable to retrieve private key " + alias, e);
        }

        return null;
    }

    /**
     * Gets the public key associated with the named certificate.
     * 
     * @param keystore the keystore to get the key from
     * @param alias the name of the certificate to get the from
     * 
     * @return the public key or null
     */
    private static PublicKey getPublicKey(KeyStore keystore, String alias) {
        alias = DatatypeHelper.safeTrimOrNullString(alias);
        if (alias == null) {
            log.error("Key alias may not be null or empty");
            System.exit(1);
        }

        try {
            Certificate cert = keystore.getCertificate(alias);
            return cert.getPublicKey();
        } catch (Exception e) {
            log.error("Unable to retrieve certificate " + alias, e);
            System.exit(1);
        }

        return null;
    }

    /**
     * Signs the given metadata document root.
     * 
     * @param metadata metadata document
     * @param signingKey key used to sign the document
     */
    private static void sign(SignableSAMLObject metadata, PrivateKey signingKey) {
        Signature signature = new SignatureBuilder().buildObject();
        SAMLObjectContentReference contentRef = new SAMLObjectContentReference(metadata);
        signature.getContentReferences().add(contentRef);
        signature.setSigningKey(signingKey);
        metadata.setSignature(signature);

        Signer.signObject(signature);
    }

    /**
     * Verifies the signatures of the metadata document.
     * 
     * @param metadata the metadata document
     * @param verificationKey the key to use to verify it
     */
    private static void verifySiganture(SignableSAMLObject metadata, PublicKey verificationKey) {
        SignatureValidator signatureValidator = new SignatureValidator(verificationKey);

        try {
            Signature signature = metadata.getSignature();
            if (signature != null) {
                signatureValidator.validate(signature);
            } else {
                log.info("Metadata root was not signed, skipping signature checking");
            }
        } catch (ValidationException e) {
            errorAndExit("Signature can not be verified", e);
        }
    }

    /**
     * Writes the given metadata out.
     * 
     * @param metadata the metadata to write
     * @param outputFile the file to write the metadata to, or null for STDOUT
     */
    private static void printMetadata(XMLObject metadata, String outputFile) {
        PrintStream out = System.out;
        
        if(outputFile != null){
            try{
                out = new PrintStream(new File(outputFile));
            }catch(Exception e){
                errorAndExit("Unable to open output file for writing", e);
            }
        }
        
        try {
            if (!DatatypeHelper.isEmpty(outputFile)) {
                File outFile = new File(outputFile);
                outFile.createNewFile();
                out = new PrintStream(new File(outputFile));
            }
        } catch (Exception e) {
            log.error("Unable to write to output file", e);
        }

        out.print(XMLHelper.nodeToString(metadata.getDOM()));
    }

    /**
     * Configures the logging for this tool. Default logging level is error.
     * 
     * @param warningEnable whether to enable warning level
     */
    private static void configureLogging() {
        ConsoleAppender console = new ConsoleAppender();
        console.setWriter(new PrintWriter(System.err));
        console.setName("stderr");
        console.setLayout(new PatternLayout("%d{ABSOLUTE} %-5p [%c{1}] %m%n"));

        log = Logger.getLogger("org.opensaml");
        log.addAppender(console);
        log.setLevel(Level.ERROR);

        Logger.getRootLogger().setLevel(Level.OFF);
    }

    /**
     * Prints a help message to the given output stream.
     * 
     * @param out output to print the help message to
     */
    private static void printHelp(PrintStream out) {
        out.println("usage: java org.opensaml.security.MetadataTool");
        out.println();
        out.println("when retrieving:");
        out.println("  --input <fileOrUrl> [--ouput <outfile>]");
        out.println("when signing:");
        out.println("  --input <fileOrUrl> --sign --keystore <keystore> [--storetype <storetype>] "
                + "--storepass <password> --alias <alias> [--keypass <password>] [--output <outfile>]");
        out.println("when retrieving and verifying signature:");
        out.println("  --input <fileOrUrl> --validate --keystore <keystore> [--storetype <storetype>] "
                + "--storepass <password> --alias <alias> [--output <outfile>]");
        out.println();
        out.println();
        out.println(String.format("  --%-16s %s", CLIParserBuilder.HELP, "print this message"));
        out.println(String.format("  --%-16s %s", CLIParserBuilder.VALIDATE,
                "validate the digital signature on the metadata if it is signed"));
        out.println(String.format("  --%-16s %s", CLIParserBuilder.SIGN,
                "sign the input file and write out a signed version"));
        out.println(String.format("  --%-16s %s", CLIParserBuilder.INPUT_FILE,
                "filesystem path or URL to fetch metadata from"));
        out.println(String.format("  --%-16s %s", CLIParserBuilder.KEYSTORE, "filesystem path to Java keystore"));
        out.println(String.format("  --%-16s %s", CLIParserBuilder.KEYSTORE_TYPE, "the keystore type (default: JKS)"));
        out.println(String.format("  --%-16s %s", CLIParserBuilder.KEYSTORE_PASS, "keystore password"));
        out.println(String.format("  --%-16s %s", CLIParserBuilder.ALIAS, "alias of signing or verification key"));
        out.println(String.format("  --%-16s %s", CLIParserBuilder.KEY_PASS, "private key password"));
        out.println(String.format("  --%-16s %s", CLIParserBuilder.OUTPUT_FILE,
                "filesystem path where metadata will be written"));
        out.println();
    }

    /**
     * Logs, as an error, the error message and exits the program.
     * 
     * @param errorMessage error message
     * @param e exception that caused it
     */
    private static void errorAndExit(String errorMessage, Exception e) {
        if (e == null) {
            log.error(errorMessage);
        } else {
            log.error(errorMessage, e);
        }
        printHelp(System.out);
        System.out.flush();
        System.exit(1);
    }

    /**
     * Helper class that creates the command line argument parser.
     */
    private static class CLIParserBuilder {

        // Command line arguments
        public static final String HELP = "help";

        public static final String SIGN = "sign";

        public static final String VALIDATE = "validate";

        public static final String INPUT_FILE = "input";

        public static final String KEYSTORE = "keystore";

        public static final String KEYSTORE_TYPE = "storetype";

        public static final String KEYSTORE_PASS = "storepass";

        public static final String ALIAS = "alias";

        public static final String KEY_PASS = "keypass";

        public static final String OUTPUT_FILE = "output";

        // Command line parser arguments
        public static CmdLineParser.Option HELP_ARG;

        public static CmdLineParser.Option SIGN_ARG;

        public static CmdLineParser.Option VALIDATE_ARG;

        public static CmdLineParser.Option INPUT_FILE_ARG;

        public static CmdLineParser.Option KEYSTORE_ARG;

        public static CmdLineParser.Option KEYSTORE_TYPE_ARG;

        public static CmdLineParser.Option KEYSTORE_PASS_ARG;

        public static CmdLineParser.Option ALIAS_ARG;

        public static CmdLineParser.Option KEY_PASS_ARG;

        public static CmdLineParser.Option OUTPUT_FILE_ARG;

        /**
         * Create a new command line parser.
         * 
         * @return command line parser
         */
        public static CmdLineParser buildParser() {
            CmdLineParser parser = new CmdLineParser();

            HELP_ARG = parser.addBooleanOption(HELP);
            SIGN_ARG = parser.addBooleanOption(SIGN);
            VALIDATE_ARG = parser.addBooleanOption(VALIDATE);
            INPUT_FILE_ARG = parser.addStringOption(INPUT_FILE);
            KEYSTORE_ARG = parser.addStringOption(KEYSTORE);
            KEYSTORE_TYPE_ARG = parser.addStringOption(KEYSTORE_TYPE);
            KEYSTORE_PASS_ARG = parser.addStringOption(KEYSTORE_PASS);
            ALIAS_ARG = parser.addStringOption(ALIAS);
            KEY_PASS_ARG = parser.addStringOption(KEY_PASS);
            OUTPUT_FILE_ARG = parser.addStringOption(OUTPUT_FILE);

            return parser;
        }
    }
}