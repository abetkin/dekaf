package org.jetbrains.jdba.postgre;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jdba.core.BaseErrorRecognizer;
import org.jetbrains.jdba.core.exceptions.DBException;
import org.jetbrains.jdba.core.exceptions.DuplicateKeyException;
import org.jetbrains.jdba.core.exceptions.UnknownDBException;

import java.sql.SQLException;



/**
 * PostgreSQL specific errors recognizer.
 **/
public class PostgreErrorRecognizer extends BaseErrorRecognizer {

  @NotNull
  @Override
  protected DBException recognizeSpecificError(@NotNull final SQLException sqlException, @Nullable final String statementText) {
    int code = sqlException.getErrorCode();

    switch (code) {
      case 1:
        return new DuplicateKeyException(sqlException, statementText);
      default:
        return new UnknownDBException(sqlException, statementText);
    }
  }
}
