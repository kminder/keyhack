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

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Enumeration;

import javax.crypto.SecretKey;

import org.apache.commons.codec.binary.Base64;

public class KeyHack {

  static Args args;

  public static final void main( String[] mainArgs ) {
    try {
      args = new Args( mainArgs );
      if( args.isShowHelp() ) {
        Args.showHelpAndExit();
      } else if( args.isListAliases() ) {
        listAliases();
      } else if( args.isExtractKey() ) {
        extractKey();
      } else if( args.isExtractAllKeys() ) {
        extractAllKeys();
      } else if( args.getAliasName() != null ){
        extractKey();
      } else {
        extractAllKeys();
      }
    } catch ( Exception e ) {
      e.printStackTrace();
    }

  }

  private static KeyStore loadKeyStore() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
    KeyStore ks = KeyStore.getInstance( args.getKeystoreType() );
    try ( FileInputStream fis = new FileInputStream( args.getKeystoreFile() ) ) {
      ks.load( fis, args.getKeystorePassword() );
    }
    return ks;
  }

  private static final void listAlias( KeyStore ks, String alias ) {
    System.out.println( alias );
  }

  private static final void listAliases() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
    KeyStore ks = loadKeyStore();
    Enumeration<String> aliases = ks.aliases();
    while( aliases.hasMoreElements() ) {
      String alias = aliases.nextElement();
      listAlias( ks, alias );
    }
  }

  private static final void extractKey( KeyStore ks, String prefix, String alias ) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
    SecretKey secretKey = (SecretKey) ks.getKey( alias, args.getAliasPassword());
    //System.out.println( secretKey.getAlgorithm() );
    //System.out.println( secretKey.getFormat() );
    System.out.println( prefix + encodeKey( secretKey.getEncoded() ) );
  }

  private static final void extractKey() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
    KeyStore ks = loadKeyStore();
    extractKey( ks, "", args.getAliasName() );
  }

  private static String encodeKey( byte[] key ) {
    if ( "b64".equalsIgnoreCase( args.getKeyEncoding() ) ) {
      return Base64.encodeBase64URLSafeString( key );
    } else {
      return new String( key );
    }
  }

  private static final void extractAllKeys() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, UnrecoverableKeyException {
    KeyStore ks = loadKeyStore();
    Enumeration<String> aliases = ks.aliases();
    while( aliases.hasMoreElements() ) {
      String alias = aliases.nextElement();
      extractKey( ks, alias+"=", alias );
    }
  }

}
