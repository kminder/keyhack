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

  static CommandLine args;

  private static Options options() {
    Options options = new Options();
    options.addOption( "h", "showHelp", false, "Show this showHelp." );
    options.addOption( "l", "list", false, "List keystore aliases." );
    options.addOption( "sf", "keystore", true, "Keystore file" );
    options.addOption( "sp", "storepass", true, "Keystore password." );
    options.addOption( "st", "storetype", true, "Keystore type." );
    options.addOption( "e", "extract", false, "Extract key." );
    options.addOption( "ea", "extractall", false, "Extract all keys." );
    options.addOption( "an", "aliasname", true, "Key alias." );
    options.addOption( "ap", "aliaspass", true, "Key password." );
    options.addOption( "ek", "extractencoding", true, "raw|b64" );
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
    return args.hasOption( "showHelp" );
  }

  boolean isListAliases() {
    return args.hasOption( "list" );
  }

  boolean isExtractKey() {
    return args.hasOption( "extract" );
  }

  boolean isExtractAllKeys() {
    return args.hasOption( "extractall" );
  }

  char[] getKeystorePassword() {
    return args.getOptionValue( "storepass", "" ).toCharArray();
  }

  String getKeystoreType() {
    if ( args.hasOption( "storetype" ) ) {
      return args.getOptionValue( "storetype" );
    } else {
      return FilenameUtils.getExtension( getKeystoreFile() );
    }
  }

  String getKeystoreFile() {
    return args.getOptionValue( "keystore" );
  }

  String getAliasName() {
    return args.getOptionValue( "aliasname" );
  }

  char[] getAliasPassword() {
    return args.getOptionValue( "aliaspass", args.getOptionValue( "storepass", "" ) ).toCharArray();
  }

  String getKeyEncoding() {
    return args.getOptionValue( "encodekey", "raw" );
  }

}