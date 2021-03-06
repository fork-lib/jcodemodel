/**
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 * Portions Copyright 2013-2016 Philip Helger + contributors
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package com.helger.jcodemodel.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;

import javax.annotation.Nonnull;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.helger.jcodemodel.IJDeclaration;
import com.helger.jcodemodel.IJExpression;
import com.helger.jcodemodel.IJGenerable;
import com.helger.jcodemodel.IJStatement;
import com.helger.jcodemodel.JCodeModel;
import com.helger.jcodemodel.JFormatter;
import com.helger.jcodemodel.writer.OutputStreamCodeWriter;
import com.helger.jcodemodel.writer.SingleStreamCodeWriter;

/**
 * Various utilities for codemodel tests.
 *
 * @author Aleksei Valikov
 * @author Philip Helger
 */
public final class CodeModelTestsHelper
{
  public static final Charset DEFAULT_ENCODING = Charset.forName ("UTF-8");

  /** Hidden constructor. */
  private CodeModelTestsHelper ()
  {}

  /**
   * Prints an expression into a string.
   *
   * @param aExpression
   *        expression to print into a string.
   * @return Expression formatted as a string.
   */
  @Nonnull
  public static String toString (@Nonnull final IJExpression aExpression)
  {
    if (aExpression == null)
      throw new IllegalArgumentException ("Generable must not be null.");

    final StringWriter aSW = new StringWriter ();
    final JFormatter formatter = new JFormatter (aSW);
    aExpression.generate (formatter);
    return aSW.toString ();
  }

  /**
   * Prints a declaration into a string.
   *
   * @param aDeclaration
   *        declaration to print into a string.
   * @return Declaration formatted as a string.
   */
  @Nonnull
  public static String toString (@Nonnull final IJDeclaration aDeclaration)
  {
    if (aDeclaration == null)
      throw new IllegalArgumentException ("Declaration must not be null.");

    final StringWriter aSW = new StringWriter ();
    final JFormatter formatter = new JFormatter (aSW);
    aDeclaration.declare (formatter);
    return aSW.toString ();
  }

  /**
   * Prints a statement into a string.
   *
   * @param aStatement
   *        declaration to print into a string.
   * @return Declaration formatted as a string.
   */
  @Nonnull
  public static String toString (@Nonnull final IJStatement aStatement)
  {
    if (aStatement == null)
      throw new IllegalArgumentException ("Statement must not be null.");

    final StringWriter aSW = new StringWriter ();
    final JFormatter formatter = new JFormatter (aSW);
    aStatement.state (formatter);
    return aSW.toString ();
  }

  @Nonnull
  public static String declare (@Nonnull final IJDeclaration declaration)
  {
    if (declaration == null)
      throw new IllegalArgumentException ("Declaration must not be null.");

    final StringWriter aSW = new StringWriter ();
    final JFormatter formatter = new JFormatter (aSW);
    declaration.declare (formatter);
    return aSW.toString ();
  }

  @Nonnull
  public static String generate (@Nonnull final IJGenerable generable)
  {
    if (generable == null)
      throw new IllegalArgumentException ("Generable must not be null.");

    final StringWriter aSW = new StringWriter ();
    final JFormatter formatter = new JFormatter (aSW);
    generable.generate (formatter);
    return aSW.toString ();
  }

  /**
   * Get the content of the code model as a byte array in
   * {@link #DEFAULT_ENCODING}
   *
   * @param cm
   *        Source code model
   * @return The byte array
   * @throws IOException
   *         Theoretically only
   */
  @Nonnull
  public static byte [] getAllBytes (@Nonnull final JCodeModel cm) throws IOException
  {
    try (final ByteArrayOutputStream bos = new ByteArrayOutputStream ())
    {
      cm.build (new OutputStreamCodeWriter (bos, DEFAULT_ENCODING));
      return bos.toByteArray ();
    }
  }

  @Nonnull
  public static CompilationUnit parseCodeModel (@Nonnull final JCodeModel cm) throws IOException, ParseException
  {
    final byte [] aBytes = getAllBytes (cm);
    if (true)
      System.out.println (new String (aBytes, DEFAULT_ENCODING.name ()));

    final ByteArrayInputStream bis = new ByteArrayInputStream (aBytes);

    return JavaParser.parse (bis, DEFAULT_ENCODING.name ());
  }

  public static void printCodeModel (@Nonnull final JCodeModel cm) throws IOException
  {
    cm.build (new SingleStreamCodeWriter (System.out));
  }
}
