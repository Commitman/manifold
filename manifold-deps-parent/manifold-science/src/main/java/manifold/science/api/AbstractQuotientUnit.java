/*
 * Copyright (c) 2019 - Manifold Systems LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package manifold.science.api;

import manifold.science.util.Rational;


import static manifold.science.util.Rational.ONE;

public abstract class AbstractQuotientUnit<A extends IUnit,
  B extends IUnit,
  D extends IDimension<D>,
  U extends AbstractQuotientUnit<A, B, D, U>> extends AbstractBinaryUnit<A, B, D, U> {

  protected AbstractQuotientUnit( A leftUnit, B rightUnit )
  {
    this( leftUnit, rightUnit, null, null, null );
  }
  protected AbstractQuotientUnit( A leftUnit, B rightUnit, Rational factor )
  {
    this( leftUnit, rightUnit, factor, null, null );
  }
  protected AbstractQuotientUnit( A leftUnit, B rightUnit, Rational factor, String name )
  {
    this( leftUnit, rightUnit, factor, name, null );
  }
  protected AbstractQuotientUnit( A leftUnit, B rightUnit, Rational factor, String name, String symbol )
  {
    super( leftUnit, rightUnit, factor, name, symbol );
  }
  
  public String getUnitName() {
    return super.getUnitName() == null
           ? getLeftUnit().getUnitName() + "/" + getRightUnit().getUnitName()
           : super.getUnitName();
  }

  public String getUnitSymbol() {
    return super.getUnitSymbol() == null
           ? getLeftUnit().getUnitSymbol() + "/" + getRightUnit().getUnitSymbol()
           : super.getUnitSymbol();
  }

  public String getFullName() {
    return getLeftUnit().getFullName() + "/" + getRightUnit().getFullName();
  }

  public String getFullSymbol() {
    return getLeftUnit().getFullSymbol() + "/" + getRightUnit().getFullSymbol();
  }

  public Rational toBaseUnits( Rational myUnits ) {
    return (getLeftUnit().toBaseUnits( ONE ) / getRightUnit().toBaseUnits( ONE )) * myUnits * getFactor();
  }

  public Rational toNumber() {
    return getLeftUnit().toNumber() / getRightUnit().toNumber();
  }

  public A multiply( B a ) {
    return getLeftUnit();
  }
}