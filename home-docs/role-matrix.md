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
| Get all Users                          | V     | V                 | V     |     |
| Create user                            |       |                   |       | V   |
| Get User by ID                         | V     | V                 | V     |     |
| Update current User                    | V     | V                 | V     |     |
| *__User contact__*                     |       |                   |       |     |
| Get all Contacts by User               | V     | V                 | V     |     |
| Create new Contact for the User        | V     | V                 | V     |     |
| Get Contact by ID                      | V     | V                 | V     |     |
| Update Contact                         | V     | V                 | V     |     |
| Delete Contact                         | V     | V                 | V     |     |
| *__Cooperation__*                      |       |                   |       |     |
| Get All Cooperations                   | V     | V                 | V     |     |
| Create Cooperation                     | V     |                   |       |     |
| Get Cooperation by ID                  | V     | V                 | V     |     |
| Update current Cooperation             | V     | V                 |       |     |
| Delete cooperation by ID               | V     |                   |       |     |
| *__House__*                            |       |                   |       |     |
| Get all Houses by Cooperation          | V     | V                 | V     |     |
| Create new House for the Cooperation   | V     | V                 |       |     |
| Get House by ID                        | V     | V                 | V     |     |
| Update current House                   | V     | V                 |       |     |
| Delete house by ID                     | V     | V                 |       |     |
| *__Apartment__*                        |       |                   |       |     |
| Get all apartments                     |       | V                 | V     |     |
| Create new Apartment for the House     |       | V                 |       |     |
| Get apartment by ID                    |       | V                 | V     |     |
| Update apartment                       |       | V                 |       |     |
| Delete apartment                       |       | V                 |       |     |
| *__Apartment Ownership__*              |       |                   |       |     |
| Get all ownerships                     |       | V                 | V     |     |
| Get ownership by ID                    |       | V                 | V     |     |
| Update ownership                       |       | V                 |       |     |
| Delete ownership                       |       | V                 |       |     |
| *__Apartment Invitation__*             |       |                   |       |     |
| Get all invitations                    |       | V                 |       |     |
| Create invitation                      |       | V                 |       |     |
| Get invitation by ID                   |       | V                 |       |     |
| Update invitation                      |       | V                 |       |     |
| Delete invitation                      |       | V                 |       |     |
| *__Cooperation Poll__*                 |       |                   |       |     |
| Get all cooperation polls              |       | V                 | V     |     |
| Create cooperation poll                |       | V                 |       |     |
| Get cooperation poll                   |       | V                 | V     |     |
| Update cooperation poll                |       | V                 |       |     |
| Delete cooperation poll                |       | V                 |       |     |
| *__Poll__*                             |       |                   |       |     |
| Get all polls                          |       | V                 | V     |     |
| Get poll                               |       | V                 | v     |     |
| *__Cooperation Contact__*              |       |                   |       |     |
| Get all Contacts by Cooperation ID     | V     | V                 | V     |     |
| Create new Contact for the Cooperation | V     | V                 |       |     |
| Get Contact by ID                      | V     | V                 | V     |     |
| Update Contact                         | V     | V                 |       |     |
| Delete Contact                         | V     | V                 |       |     |
| *__Poll Question__*                    |       |                   |       |     |
| Get all questions                      |       | V                 | V     |     |
| Create question                        |       | V                 |       |     |
| Get question                           |       | V                 | V     |     |
| Update question                        |       | V                 |       |     |
| Delete question                        |       | V                 |       |     |

See more info in [google docs](https://docs.google.com/spreadsheets/d/1kKI9s_mpG3x30oWkVwgl34n7tnQsx3QgsDiI61fYJvo/edit?usp=sharing).
