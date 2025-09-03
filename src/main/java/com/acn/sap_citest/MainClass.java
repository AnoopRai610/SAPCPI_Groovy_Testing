package com.acn.sap_citest;

//Code creates as a src/MainClass.java file to run as Java Application

import com.sap.cpi.test.RunScript;
import com.sap.gateway.ip.core.customdev.util.Message;

public class MainClass {

    public static void main(String[] args) throws Exception {
    	

        // Path to the message file (used as input body)
        String messagePath = "./testFile.json";
        
        /**
         * In case of you want to upload message body, header & attachment and exchange property
         * using files from a directory, then provide directory name. Files need to present with
         * below names (contains) will be consider and upload:
         * a. body
         * b. attachment
         * c. header
         * d. property
         */
        //String messagePath = "./messageDirectory";

        // Path to the Groovy script to be tested
        String scriptPath = "./src/main/java/com/acn/sap_citest/TestScript.groovy";

        // Initialize the script runner
        RunScript runScript = new RunScript(messagePath, scriptPath);

        // Load value mappings if needed
        runScript.loadValuemapping("C:\\Users\\anoop.kumar.rai\\Downloads\\2025-01-07\\EAI_BASF_DIL_BASF_CASS_VM.zip");

        // Retrieve the message object before executing
        Message message = runScript.getMessage();

        // Set message headers
        message.setHeader("partnerName", "PARTNER1");
        message.setHeader("IDOCType", "MBGMCR");

        // Set exchange properties
        message.setProperty("sourceAgency", "Sender");
        message.setProperty("sourceIdentifier", "CUSTNUM");
        message.setProperty("sourceValue", "VAL");
        message.setProperty("abc", "1234");

        // Execute the script method (default is "processData")
        message = runScript.invokeMethod("processData");

        // Print the transformed message to console
        System.out.println("Transformed message:");
        System.out.println(message.getBody());

        // Optionally write the output to a file
        /*
        FileOutputStream out = new FileOutputStream(messagePath + ".out");
        out.write(message.getBody(String.class).getBytes());
        out.close();
        */
    }
}

