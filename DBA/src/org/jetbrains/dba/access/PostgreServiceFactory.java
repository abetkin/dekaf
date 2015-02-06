package org.jetbrains.dba.access;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.dba.KnownRdbms;
import org.jetbrains.dba.Rdbms;
import org.jetbrains.dba.sql.SQL;

import javax.sql.DataSource;
import java.util.regex.Pattern;



/**
 * @author Leonid Bushuev from JetBrains
 */
public class PostgreServiceFactory implements DBServiceFactory {

  private final SQL mySQL = new SQL();
  private final PostgreErrorRecognizer myErrorRecognizer = new PostgreErrorRecognizer();
  private final Pattern myConnectionStringPattern = Pattern.compile("^jdbc:postgresql:.*$");


  @NotNull
  @Override
  public Rdbms rdbms() {
    return KnownRdbms.POSTGRE;
  }


  @NotNull
  @Override
  public SQL cloneSQL() {
    return mySQL.clone();
  }


  @NotNull
  @Override
  public PostgreErrorRecognizer errorRecognizer() {
    return myErrorRecognizer;
  }


  @NotNull
  @Override
  public Pattern connectionStringPattern() {
    return myConnectionStringPattern;
  }


  @NotNull
  @Override
  public PostgreFacade createFacade(@NotNull DataSource source) {
    return new PostgreFacade(KnownRdbms.POSTGRE, source, myErrorRecognizer, cloneSQL());
  }

}