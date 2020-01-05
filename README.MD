Changelog/ADRs/diary:

#### Gas stations

1. I have created the gas stations module first. It provides information about gas stations on map - 
reviews and gas prices. Module consists of two main aggregates - gas station and observed gas station 
for the use for user notification. This module was made using a package
private scope domain with public facade object with business and persistence layer. 
I think no more layers are needed now as the module is simple enough
A CQRS approach was not used as this module is a small CRUD.

2. I wanted to use cucumber test framework, but it has some problems with groovy and bugs out in intellij.
Now, cucumber features helps me prepare some use cases for groovy unit test.

3. I decided to perform a mutation tests to make sure that my unit tests are any good. 
I ignored testing lombok's generated methods.

4. I have made some architecture tests to make sure my domain is not leaking outside the package.

5. Added my first HATEOAS links