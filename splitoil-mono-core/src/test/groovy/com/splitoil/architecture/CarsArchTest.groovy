package com.splitoil.architecture

import com.splitoil.ArchTest
import com.splitoil.gasstation.domain.GasStationsFacade
import com.splitoil.shared.event.DomainEvent
import com.tngtech.archunit.core.importer.ClassFileImporter
import org.junit.experimental.categories.Category
import spock.lang.Specification

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes

@Category(ArchTest)
class CarsArchTest extends Specification {

    def "domain is sealed"() {
        expect:
            def domain = new ClassFileImporter().importPackages("com.splitoil.car.domain")

            def domainRule = classes().that()
                    .resideInAPackage("com.splitoil.car.domain")
                    .and()
                    .doNotHaveSimpleName("CarFacade")
                    .and()
                    .doNotImplement(DomainEvent)
                    .should()
                    .onlyBeAccessed()
                    .byAnyPackage("com.splitoil.car.domain")

            domainRule.check(domain)
    }

    def "domain is package scope"() {
        expect:
            def domain = new ClassFileImporter().importPackages("com.splitoil.car.domain")

            def domainRule = classes().that()
                    .resideInAPackage("com.splitoil.car.domain")
                    .and()
                    .doNotHaveSimpleName("CarFacade")
                    .and()
                    .doNotImplement(DomainEvent)
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
                    .haveNameMatching("CarFacade")
                    .should()
                    .bePublic()

            domainRule.check(domain)
    }

    def "controller is not referenced"() {
        expect:
            def domain = new ClassFileImporter().importPackages("com.splitoil.car")

            def domainRule = classes().that()
                    .haveNameMatching("CarController")
                    .should()
                    .bePackagePrivate()

            domainRule.check(domain)
    }
}
