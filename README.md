### Objective of the project: 
Develop a RESTful API for a social media platform that allows users to register, log in, create posts, chat, follow other users and get their activity feed.
### Requirements: 
#### 1.  Authentication and Authorization: 
- Users can register by providing a username, email and password. 
- Users can sign in by providing the correct credentials.
- The API must protect the privacy of user data, including password hashing and the use of JWTs.

#### 2. Post management: 
 - Users can create new posts by providing text, a title, and attaching images.
 - Users can view posts by other users. 
 - Users can update and delete their own posts. 

#### 3. Users interaction: 
- Users can send friend requests to other users. From this moment, the user who sent the request remains a subscriber until he unsubscribes himself. If the user who received the request accepts it, both users become friends. If rejected, then the user who sent the application, as indicated earlier, still remains a subscriber. 
- Users who are friends are also followers of each other. 
- If one of the friends removes another from friends, then he also unsubscribes. The second user must remain a subscriber.
- Friends can write messages to each other (implementation of the chat is not needed, users can request correspondence using a request)

#### 4. Subscriptions and activity feed: 
- The user's activity feed should display the latest posts from the users they follow.
-  

#### 5. Error processing: 
- The API should handle and return understandable error messages for bad requests or internal server problems.
- The API should validate the entered data and return informative messages if the format is incorrect. 

#### 6. API Documentation: 
- The API should be well documented using tools like Swagger or OpenAPI.
- The documentation should contain descriptions of available endpoints, request and response formats, and authentication requirements. 


### Technologies and tools: 
- Programming language: Java 
- Framework: Spring (Spring Boot is recommended)
- Database: Recommended to use PostgreSQL or MySQL
- Authentication and Authorization: Spring Security 
- API documentation: Swagger or OpenAPI


### Expected results:
- A RESTful API developed that can fulfill the specified requirements. 
- Well-structured and documented project code. 
- Tests covering the core functionality of the API. 
- API documentation describing available endpoints and their usage.


#### Some Factors Affecting the Rating: 
- Completeness of compliance with the requirements;
- Code scalability and adherence to important development principles; 
- Code readability.