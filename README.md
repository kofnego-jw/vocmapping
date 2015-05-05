# vocmapping
Mapping Vocabularies

This application should help people map lots of vocabularies at once. After build of the at.ac.uibk.igwee.metadata
subfolder, you should find a file in

at.ac.uibk.igwee.metadata/at.ac.uibk.igwee.metadata.webapp.vocmapper/target/
at.ac.uibk.igwee.webapp.metadata.mdmapper-1.0-SNAPSHOT.jar

use the following command to start the application

java -jar at.ac.uibk.igwee.webapp.metadata.mdmapper-1.0-SNAPSHOT.jar

And open your browser to "http://localhost:13116/"

The web application should be available. The queries in VIAF and Wikidata should be available. To be able to use
GND and Geonames, you will need to apply to access.


## Using Geonames and GND

To be able to use this application with GND (Gesamte Normdatei) of the German National Library and with Geonames,
you will need to apply for access of these services.


### Geonames
For Geonames: Please create a file called

userinfo.properties

And add the following information to it:

org.geonames.username = [your usename]

By doing so, the access to Geonames will be made through the username provided in the file.


### GND
For GND you need to apply for an IP based access to the searching services. If you should have an token based
access to GND, you will need to modify the following file:

at.ac.uibk.igwee.metadata/at.ac.uibk.igwee.metadata.gnd/src/main/java/at/ac/uibk/igwee/metadata/gnd/impl/GndQueryServiceImpl

and add the following line to the DEFAULT_PARAMS_QUERY (at line 41)

    new ParameterPair("accessToken", "[YOUR ACCESSTOKEN]"),

By doing so, the query for DNB a new parameter "accessToken" with the token you provided will be added to all queries.


## Extending the framework
Basically, if you want to extend the searching framework, you will need to add new modules/packages:

1. Implement the Authority interface
The Authority interface lies in at.ac.uibk.igwee.metadata.vocabulary folder.

    at.ac.uibk.igwee.metadata.vocabulary.Authority

Using a singleton might help.

2. Implement the Vocabulary interface.
The Vocabulary interface lies in at.ac.uibk.igwee.metadata.vocabulary folder.

    at.ac.uibk.igwee.metadata.vocabulary.Vocabulary

You can also extends the AbstractVocabulary class.

3 Implement the QueryService interface.
The QueryService interface defines several methods which are needed for the MetaQueryService. The interface is defined
in

    at.ac.uibk.igwee.metadata.query.QueryService

If you are familiar with OSGi programming, you should annotate the implementation class with @Component which provides
the QueryService interface. By doing so, the MetaQueryServiceImpl will automatically pick up the implementation class
and use the service for querying purpose.

The xslt.api, xslt.impl and at.ac.uibk.igwee.metadata.httpclient packages are helper packages. These packages provide
abstractions for XSL-Transformations and HttpClient.


## Adding features to the web application.

The application is implemented with Spring Boot, i.e. without OSGi. Therefore one must adapt the Spring configuration
in order to use the newly added features. You will have to do the following in the
at.ac.uibk.igwee.metadata.webapp.vocmapper folder:

1. Add the dependencies to the pom.xml
2. Add the necessary configuration in the
    at.ac.uibk.igwee.webapp.metadata.mdmapper.application.Application class.
3. Change the following places, so the application can add new QueryServices:
    resources/static/resources/js/app.js (line 327)
        You will need to provide a name (which is also sent to the server), an url (for preview purpose) and an
        attribute named "selected".
    java/at/ac/uibk/igwee/webapp/metadata/mdmapper/controller/PreQueryController (line 180)
        You will need to provide a mapper for the implemented Authority.

The source code is licensed with BSD two clause license.

