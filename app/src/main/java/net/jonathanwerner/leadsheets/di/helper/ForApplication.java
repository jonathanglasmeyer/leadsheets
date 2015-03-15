package net.jonathanwerner.leadsheets.di.helper; /**
 * Created by jwerner on 2/17/15.
 */

import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier @Retention(RUNTIME)
public @interface ForApplication {
}
