package com.mballen.demoparkapi.util;


import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Component
public class ServletUriBuilderImpl implements UriBuilder{
    @Override
    public URI build(String path, Object... params) {
        return ServletUriComponentsBuilder.fromCurrentRequestUri().path(path).buildAndExpand(params).toUri();
    }
}
