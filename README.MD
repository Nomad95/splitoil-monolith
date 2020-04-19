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

6. I have decided that petrol price should be an separate entity by the need to be easily searchable.
 GasStation entity doesn't have to have consistent prices
 
#### Cars

1. I made three aggregates: Car itself (definition, and travel summaries), car cost (how much we spend on car maintenance)
 and a car refuel (how much we spend on refuels). I think costs and refuels do not have
to be consistent - this data doesn't change together so it doesn't have to be inside Car aggregate 

2. I made all necessary tests just like in GasStations module

3. I have created sqls for tests

4. I have added the domain events as well as a publisher class


#### Lobby 

Lobby need to be created before travel starts and remains until it ends. Lobby is used
for participants chat, and for brief travel configuration. Additionally lobby is used to 
send invitations for users and to add participants manually (When they are not registered 
in application but are needed for fee distribution). In lobby, lobby creator defines travel plan that is 
visible to other users in lobby. When lobby creator accepts plan a Travel is created.  


#### Mutation tests 

I am skipping some mutation test outputs as its too time-consuming and this project is not meant to be fully working. This projects focuses more on DDD 
not on tests. Mutation tests are here only to experiment with various frameworks and libraries.