## Role Matrix
We have the following roles defined in the application:

- __ADMIN__ - The most powerful role which includes possibility to manage application, create cooperation, add houses, create new cooperation_admin.
- __COOPERATION_ADMIN__ - The role that can be used for managing his cooperation.
- __OWNER__ - An owner of apartment could be in several cooperations.
- __ANY__ - Any possible user of the application including unregistered ones.

| Action                                 | ADMIN | COOPERATION_ADMIN | OWNER | ANY |
|:---------------------------------------|:-----:|:-----------------:|:-----:|:---:|
| *__News__*                             |       |                   |       |     |
| Get all News to the home page          | V     | V                 | V     |     |
| Add a News to the home page            | V     | V                 |       |     |
| Get an existing news by its ID         | V     | V                 | V     |     |
| Update an existing News                | V     | V                 |       |     |
| Delete the chosen news                 | V     | V                 |       |     |
| *__User__*                             |       |                   |       |     |
| Get all Users                          | V     | V                 |       |     |
| Create user                            | V     |                   |       | V   |
| Get User by ID                         | V     | V                 | V     |     |
| Update current User                    | V     | V                 | V     |     |
| Delete user by ID                      | V     | V                 | V     |     |
| *__Contact__*                          |       |                   |       |     |
| Get all Contacts by User               | V     | V                 | V     |     |
| Create new Contact for the User        | V     | V                 | V     |     |
| Get Contact by ID                      | V     | V                 | V     |     |
| Update Contact                         | V     | V                 | V     |     |
| Delete Contact                         | V     | V                 | V     |     |
| *__Cooperation__*                      |       |                   |       |     |
| Get All Cooperations                   | V     |                   |       |     |
| Create Cooperation                     | V     |                   |       |     |
| Get Cooperation by ID                  | V     | V                 |       |     |
| Update current Cooperation             | V     | V                 |       |     |
| Delete cooperation by ID               | V     |                   |       |     |
| *__House__*                            |       |                   |       |     |
| Get all Houses by Cooperation          | V     | V                 |       |     |
| Create new House for the Cooperation   | V     | V                 |       |     |
| Get House by ID                        | V     | V                 |       |     |
| Update current House                   | V     | V                 |       |     |
| Delete house by ID                     | V     | V                 |       |     |
| *__Apartment__*                        |       |                   |       |     |
| Get all apartments                     | V     | V                 |       |     |
| Create new Apartment for the House     | V     | V                 |       |     |
| Get apartment by ID                    | V     | V                 |       |     |
| Update apartment                       | V     | V                 |       |     |
| Delete apartment                       | V     | V                 |       |     |
| *__Apartment Ownership__*              |       |                   |       |     |
| Get all ownerships                     | V     | V                 |       |     |
| Get ownership by ID                    | V     | V                 |       |     |
| Update ownership                       | V     | V                 |       |     |
| Delete ownership                       | V     | V                 |       |     |
| *__Apartment Invitation__*             |       |                   |       |     |
| Get all invitations                    | V     | V                 |       |     |
| Create invitation                      | V     | V                 |       |     |
| Get invitation by ID                   | V     | V                 |       |     |
| Update invitation                      | V     | V                 |       |     |
| Delete invitation                      | V     | V                 |       |     |
| *__Cooperation Poll__*                 |       |                   |       |     |
| Get all cooperation polls              | V     | V                 | V     |     |
| Create cooperation poll                | V     | V                 |       |     |
| Get cooperation poll                   | V     | V                 | V     |     |
| Update cooperation poll                | V     | V                 |       |     |
| Delete cooperation poll                | V     | V                 |       |     |
| *__Poll__*                             |       |                   |       |     |
| Get all polls                          | V     | V                 | V     |     |
| Get poll                               | V     | V                 | V     |     |
| *__Cooperation Contact__*              |       |                   |       |     |
| Get all Contacts by Cooperation ID     | V     | V                 | V     |     |
| Create new Contact for the Cooperation | V     | V                 |       |     |
| Get Contact by ID                      | V     | V                 | V     |     |
| Update Contact                         | V     | V                 |       |     |
| Delete Contact                         | V     | V                 |       |     |
| *__Poll Question__*                    |       |                   |       |     |
| Get all questions                      | V     | V                 | V     |     |
| Create question                        | V     | V                 |       |     |
| Get question                           | V     | V                 | V     |     |
| Update question                        | V     | V                 |       |     |
| Delete question                        | V     | V                 |       |     |
