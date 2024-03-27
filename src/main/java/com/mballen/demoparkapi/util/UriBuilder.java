package com.mballen.demoparkapi.util;

import java.net.URI;

public interface UriBuilder {
    URI build(String path, Object... params);
}
