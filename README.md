# Friends Management APP

## In this is an application "Friends Management" is a common requirement. The application has features like "Friend Connection", “Subscribe”, “Unsubscribe", “Common Friend List”, “Subscriber List” "Block", "Receive Updates" etc.

# Technology Choice
## Spring Boot
1.	Spring Boot allows easy setup of standalone Spring-based applications.
2.	Ideal for spinning up microservices and easy to deploy.
3.	Makes data access less of a pain, i.e. JPA mappings through Spring Data.
## JDBC Template
1.	Spring JdbcTemplate is a powerful mechanism to connect to the database and execute SQL queries. 
2.	It internally uses JDBC api, but eliminates a lot of problems of JDBC API.
## H2 Database (In memory database)
1.	H2 is an open-source lightweight Java database. 
2.	H2 database can be configured to run as in-memory database, which means that data will not persist on the disk.
## Swagger
1.	Swagger is a framework for describing API using a common language that everyone can understand.
2.	The Swagger spec standardizes API practices, how to define parameters, paths, responses, models, etc.
## PCF
1.	Cloud Foundry is an open source, multi-cloudapplication platform as a service (PaaS) governed by the Cloud Foundry Foundation organization
2.	The software was originally developed by VMware and then transferred to Pivotal Software, a joint venture by EMC, VMware and General Electric.
3.	This gives a chance to try out PCF in our software
The Application is deployed on Pivotal cloud. This is the URL
https://sps-app.cfapps.io/friendmgt/swagger-ui.html

## List of REST Endpoints and Explanation

   1.I need an API to create a friend connection between two email addresses.

     a.Method: POST
     b.Header: Content Type – Application/Json
     c.Request body: 
       {
	       "requestor":"vishu@gmail.com",
	       "target":"prashant@gmail.com"
	     }
    d.	Response body
      {
         "status": "Success",
         "description": "Successfully connected"
      }

     Defined Errors:
     •200: Success.
     •400: Bad Request

   2. I need an API to retrieve the friends list for an email address.

     a.Method:POST
     b.Header:Content Type – Application/Json
     c.Request body: 
      {
        "email":"gaurav@gmail.com "
      }
    d.Response body
       {
        "status": "Success",
        "count": 2,
        "friends": [
                     "prashant@gmail.com ",
                     "vishu@gmail.com " 
                    ]
       }


    Defined Errors:
     •200: Success.
     •400: Bad Request

   3.	I need an API to retrieve the common friends list between two email addresses. 

    a.Method: POST
    b.Header: Content Type – Application/Json
    c.Request body: 
        {
         "friends": [
                    "vishu@gmail.com ",
                   "prashant@gmail.com "
                   ]
        }
    d.Response body
              {
               "status": "Success",
               "count": 1,
               "friends":[
               "gaurav@ gmail.com 
               ]
              }

     Defined Errors:
    •200: Success.
    •400: Bad Request

   4. I need an API to subscribe to updates from an email address.
      
    a.Method: POST
    b.Header: Content Type – Application/Json
    c.Request body:
    {
    "requestor":"vishu@ gmail.com ",
    "target":"prashant@ gmail.com "
             }
    d.Response body
     {
     "status": "Success",
     "description": "Subscribed successfully"
     }

      Defined Errors:
      •200: Success.
      •400: Bad Request

   5. I need an API to block updates from an email address.

    a.Method: POST
    b.Header: Content Type – Application/Json
    c.Request body: 
    {
    "requestor":"vishu@ gmail.com ",
    "target":"gaurav@ gmail.com "
    }
    d.Response body
       {
       "status": "Success",
       "description": "Unsubscribed successfully"
       }

    Defined Errors:
      •	200: Success.
      •	400: Bad Request

  6. I need an API to retrieve all email addresses that can receive updates from an email address.
 
    a.Method: POST
    b.Header: Content Type – Application/Json
    c.Request body:
    {
    "requestor":"vishu@ gmail.com ",
    "target":"gaurav@ gmail.com "
    }
    
    d.Response body
    {
    "status": "Success",
    "description": "Unsubscribed successfully"
    }

    Defined Errors:
    •200: Success.
    •400:Bad Request


