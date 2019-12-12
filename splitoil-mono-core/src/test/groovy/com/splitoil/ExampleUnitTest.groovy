package com.splitoil

import org.junit.experimental.categories.Category
import spock.lang.Specification

@Category(UnitTest)
class ExampleUnitTest extends Specification {

    def "one plus one equals two"() {
        expect:
            1 + 1 == 2
    }

    def "lombok works"() {
        given:
            def clazz = new SomeClass()
            clazz.setValue("value")
            clazz.value = "push me"
        expect:
            clazz.value == "push me"
    }
}
