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

  private static String CMD_HELP = "help";
  private static String CMD_LIST = "list";
  private static String CMD_EXTRACT = "extract";
  private static String CMD_EXTRACTALL = "extractall";
  private static String OPT_KEYSTORE = "keystore";
  private static String OPT_KEYSTOREPASS = "keystorepass";
  private static String OPT_KEYSTORETYPE = "keystoretype";
  private static String OPT_ALIAS = "alias";
  private static String OPT_KEYPASS = "keypass";
  private static String OPT_KEYFORMAT = "keyformat";


  private static Options options() {
    Options options = new Options();
    options.addRequiredOption( "f", OPT_KEYSTORE, true, "Keystore file. Required." );
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
    formater.printHelp( "java -jar keyhack.jar", OPTIONS );
    System.exit(1);
  }

  private static CommandLine parse( String[] args ) {
    CommandLineParser parser = new DefaultParser();
    CommandLine cmd = null;
    try {
      cmd = parser.parse( OPTIONS, args);
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

  String getKeystoreFile() {
    return args.getOptionValue( OPT_KEYSTORE );
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