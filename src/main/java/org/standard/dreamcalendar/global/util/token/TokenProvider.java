package org.standard.dreamcalendar.global.util.token;

public interface TokenProvider {
    String generate(Long id) throws Exception;
    String generate(Long id, String timeUnit, Long duration) throws Exception;
}
