package com.axellience.vuegwtexamples.client.examples.recursive;

import com.axellience.vuegwt.client.Vue;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.axellience.vuegwt.jsr69.component.annotations.Prop;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsType
@Component(name = "recursive")
public class RecursiveComponent extends Vue {
    @Prop
    public Integer counter;

    @Override
    public void created() {
        if (this.counter == null)
            this.counter = 0;
    }
}
