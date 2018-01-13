/*
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
*/
package net.minder.keyhack;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FilenameUtils;

public class Args {

  private static Options OPTIONS = options();

  private static CommandLine args;

  private static final String CMD_HELP = "help";
  private static final String CMD_LIST = "list";
  private static final String CMD_EXTRACT = "extract";
  private static final String CMD_EXTRACTALL = "extractall";
  private static final String OPT_KEYSTORE = "keystore";
  private static final String OPT_KEYSTOREPASS = "keystorepass";
  private static final String OPT_KEYSTORETYPE = "keystoretype";
  private static final String OPT_ALIAS = "alias";
  private static final String OPT_KEYPASS = "keypass";
  private static final String OPT_KEYFORMAT = "keyformat";


  private static Options options() {
    Options options = new Options();
    options.addRequiredOption("f", OPT_KEYSTORE, true, "Keystore file. Required." );
    options.addOption( "h", CMD_HELP, false, "Show this help." );
    options.addOption( "l", CMD_LIST, false, "List all keystore key aliases." );
    options.addOption( "e", CMD_EXTRACT, false, "Extract keystore key." );
    options.addOption( "ea", CMD_EXTRACTALL, false, "Extract all keys." );
    options.addOption( "p", OPT_KEYSTOREPASS, true, "Keystore password. Defaults to empty string." );
    options.addOption( "t", OPT_KEYSTORETYPE, true, "Keystore type. Defaults to extension of keystore file." );
    options.addOption( "a", OPT_ALIAS, true, "Key alias." );
    options.addOption( "kp", OPT_KEYPASS, true, "Key password. Defaults to store password." );
    options.addOption( "kf", OPT_KEYFORMAT, true, "Extracted key display format. Supports raw or b64. Defaults to raw." );
    return options;
  }

  static void showHelpAndExit() {
    HelpFormatter formater = new HelpFormatter();
    formater.printHelp( "java -jar keyhack.jar", OPTIONS, true );
    System.exit(1);
  }

  private static boolean validate( CommandLine args ) {
    boolean valid = true;
    String keystore = args.getOptionValue( OPT_KEYSTORE );
    File keystoreFile = new File( keystore );
    if ( !keystoreFile.exists() ) {
      System.out.println( "ERROR: Keystore file does not exist: " + keystore );
      valid = false;
    } else {
      if( !keystoreFile.canRead() ) {
        System.out.println( "ERROR: Keystore file unreadable: " + keystore );
        valid = false;
      } else {
        if( !keystoreFile.isFile() ) {
          System.out.println( "ERROR: Keystore file invalid: " + keystore );
          valid = false;
        }
      }
    }
    return valid;
  }

  private static CommandLine parse( String[] args ) {
    CommandLineParser parser = new DefaultParser();
    CommandLine cmd = null;
    try {
      cmd = parser.parse( OPTIONS, args);
      if ( !validate( cmd ) ) {
        showHelpAndExit();
      }
    } catch ( ParseException e ) {
      showHelpAndExit();
    }
    return cmd;
  }

  Args( String[] argv ) {
    args = parse( argv );
  }

  boolean isShowHelp() {
    return args.hasOption( CMD_HELP );
  }

  boolean isListAliases() {
    return args.hasOption( CMD_LIST );
  }

  boolean isExtractKey() {
    return args.hasOption( CMD_EXTRACT );
  }

  boolean isExtractAllKeys() {
    return args.hasOption( CMD_EXTRACTALL );
  }

  String getKeystoreFile() {
    return args.getOptionValue( OPT_KEYSTORE );
  }

  char[] getKeystorePassword() {
    return args.getOptionValue( OPT_KEYSTOREPASS, "" ).toCharArray();
  }

  String getKeystoreType() {
    if ( args.hasOption( OPT_KEYSTORETYPE ) ) {
      return args.getOptionValue( OPT_KEYSTORETYPE );
    } else {
      return FilenameUtils.getExtension( getKeystoreFile() );
    }
  }

  String getAliasName() {
    return args.getOptionValue( OPT_ALIAS );
  }

  char[] getAliasPassword() {
    return args.getOptionValue( OPT_KEYPASS, args.getOptionValue( OPT_KEYSTOREPASS, "" ) ).toCharArray();
  }

  String getKeyEncoding() {
    return args.getOptionValue( OPT_KEYFORMAT, "raw" );
  }

}