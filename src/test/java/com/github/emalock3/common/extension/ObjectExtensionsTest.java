/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.emalock3.common.extension;

import java.util.Optional;
import lombok.experimental.ExtensionMethod;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

@ExtensionMethod({ObjectExtensions.class})
public class ObjectExtensionsTest {
    
    @Test
    public void testOrObjectObject() {
        assertThat("hoge".or("foo"), is("hoge"));
        assertThat(((String) null).or("foo"), is("foo"));
    }
    
    @Test
    public void testOptObject() {
        assertThat("hoge".opt(), is(Optional.of("hoge")));
        assertThat(((String) null).opt(), is(Optional.empty()));
    }
}
