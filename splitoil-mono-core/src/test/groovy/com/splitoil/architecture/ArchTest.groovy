package com.splitoil.architecture

import com.splitoil.UnitTest
import com.splitoil.gasstation.domain.GasStationsFacade
import com.tngtech.archunit.core.importer.ClassFileImporter
import org.junit.experimental.categories.Category
import spock.lang.Specification

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes

@Category(UnitTest)
class ArchTest extends Specification {

    def "domain is sealed"() {
        expect:
            def domain = new ClassFileImporter().importPackages("com.splitoil.gasstation.domain")

            def domainRule = classes().that()
                    .resideInAPackage("com.splitoil.gasstation.domain")
                    .and()
                    .doNotHaveSimpleName("GasStationsFacade")
                    .should()
                    .onlyBeAccessed()
                    .byAnyPackage("com.splitoil.gasstation.domain")

            domainRule.check(domain)
    }

    def "domain is package scope"() {
        expect:
            def domain = new ClassFileImporter().importPackages("com.splitoil.gasstation.domain")

            def domainRule = classes().that()
                    .resideInAPackage("com.splitoil.gasstation.domain")
                    .and()
                    .doNotHaveSimpleName("GasStationsFacade")
                    .and()
                    .haveNameNotMatching(".*Test")
                    .and()
                    .haveNameNotMatching(".*closure.*")
                    .should()
                    .bePackagePrivate()

            domainRule.check(domain)
    }

    def "facade is published"() {
        expect:
            def domain = new ClassFileImporter().importClasses(GasStationsFacade)

            def domainRule = classes().that()
                    .haveNameMatching("GasStationsFacade")
                    .should()
                    .bePublic()

            domainRule.check(domain)
    }

    def "controller is not referenced"() {
        expect:
            def domain = new ClassFileImporter().importPackages("com.splitoil.gasstation")

            def domainRule = classes().that()
                    .haveNameMatching("GasStationController")
                    .should()
                    .bePackagePrivate()

            domainRule.check(domain)
    }
}
