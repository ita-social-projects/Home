## Role Matrix
We have the following roles defined in the application:

- __ADMIN__ - The most powerful role which includes possibility to manage application, create cooperation, add houses, create new cooperation_admin.
- __COOPERATION_ADMIN__ - The role that can be used for managing his cooperation.
- __OWNER__ - An owner of apartment could be in several cooperations.
- __ANY__ - Any possible user of the application including unregistered ones.

A - All rights
C - Only current cooperation
S - Only for current user
I - Only by invitation

| Action                                 | ADMIN | COOPERATION_ADMIN | OWNER | ANY |
|:---------------------------------------|:-----:|:-----------------:|:-----:|:---:|
| *__News__*                             |       |                   |       |     |
| Get all News to the home page          | A     | C                 | C     |     |
| Add a News to the home page            | A     | C                 |       |     |
| Get an existing news by its ID         | A     | C                 | C     |     |
| Update an existing News                | A     | C                 |       |     |
| Delete the chosen news                 | A     | C                 |       |     |
| *__User__*                             |       |                   |       |     |
| Get all Users                          | A     | C                 |       |     |
| Create user                            | A     |                   |       | I   |
| Get User by ID                         | A     | C                 | S     |     |
| Update current User                    | A     | S                 | S     |     |
| Delete user by ID                      | A     | S                 | S     |     |
| *__Contact__*                          |       |                   |       |     |
| Get all Contacts by User               | A     | C                 | S     |     |
| Create new Contact for the User        | A     | S                 | S     |     |
| Get Contact by ID                      | A     | C                 | S     |     |
| Update Contact                         | A     | S                 | S     |     |
| Delete Contact                         | A     | S                 | S     |     |
| *__Cooperation__*                      |       |                   |       |     |
| Get All Cooperations                   | A     |                   |       |     |
| Create Cooperation                     | A     |                   |       |     |
| Get Cooperation by ID                  | A     | C                 |       |     |
| Update current Cooperation             | A     | C                 |       |     |
| Delete cooperation by ID               | A     |                   |       |     |
| *__House__*                            |       |                   |       |     |
| Get all Houses by Cooperation          | A     | C                 |       |     |
| Create new House for the Cooperation   | A     | C                 |       |     |
| Get House by ID                        | A     | C                 |       |     |
| Update current House                   | A     | C                 |       |     |
| Delete house by ID                     | A     | C                 |       |     |
| *__Apartment__*                        |       |                   |       |     |
| Get all apartments                     | A     | C                 |       |     |
| Create new Apartment for the House     | A     | C                 |       |     |
| Get apartment by ID                    | A     | C                 |       |     |
| Update apartment                       | A     | C                 |       |     |
| Delete apartment                       | A     | C                 |       |     |
| *__Apartment Ownership__*              |       |                   |       |     |
| Get all ownerships                     | A     | C                 |       |     |
| Get ownership by ID                    | A     | C                 |       |     |
| Update ownership                       | A     | C                 |       |     |
| Delete ownership                       | A     | C                 |       |     |
| *__Apartment Invitation__*             |       |                   |       |     |
| Get all invitations                    | A     | C                 |       |     |
| Create invitation                      | A     | C                 |       |     |
| Get invitation by ID                   | A     | C                 |       |     |
| Update invitation                      | A     | C                 |       |     |
| Delete invitation                      | A     | C                 |       |     |
| *__Cooperation Poll__*                 |       |                   |       |     |
| Get all cooperation polls              | A     | C                 | C     |     |
| Create cooperation poll                | A     | C                 |       |     |
| Get cooperation poll                   | A     | C                 | C     |     |
| Update cooperation poll                | A     | C                 |       |     |
| Delete cooperation poll                | A     | C                 |       |     |
| *__Poll__*                             |       |                   |       |     |
| Get all polls                          | A     | C                 | C     |     |
| Get poll                               | A     | C                 | C     |     |
| *__Cooperation Contact__*              |       |                   |       |     |
| Get all Contacts by Cooperation ID     | A     | C                 | C     |     |
| Create new Contact for the Cooperation | A     | C                 |       |     |
| Get Contact by ID                      | A     | C                 | C     |     |
| Update Contact                         | A     | C                 |       |     |
| Delete Contact                         | A     | C                 |       |     |
| *__Poll Question__*                    |       |                   |       |     |
| Get all questions                      | A     | C                 | C     |     |
| Create question                        | A     | C                 |       |     |
| Get question                           | A     | C                 | C     |     |
| Update question                        | A     | C                 |       |     |
| Delete question                        | A     | C                 |       |     |
