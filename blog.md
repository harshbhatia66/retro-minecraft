# Assignment II Pair Blog Template

## Task 1) Code Analysis and Refactoring ⛏️

### a) From DRY to Design Patterns

[Links to your merge requests](/put/links/here)

> i. Look inside src/main/java/dungeonmania/entities/enemies. Where can you notice an instance of repeated code? Note down the particular offending lines/methods/fields.

[Answer]

> ii. What Design Pattern could be used to improve the quality of the code and avoid repetition? Justify your choice by relating the scenario to the key characteristics of your chosen Design Pattern.

[Answer]

> iii. Using your chosen Design Pattern, refactor the code to remove the repetition.

[Briefly explain what you did]

### b) Observer Pattern

> Identify one place where the Observer Pattern is present in the codebase, and outline how the implementation relates to the key characteristics of the Observer Pattern.

The observer pattern is present with the Switch and Bomb classes. Here, the subject is Switch and the observer is Bomb. The subject stores a list of its observers which can be modified using a subscribe/unsubscribe method within the subject. Whenever some changes are made in the subject (in this case, the addition of a bomb given a certain map, or the onOverlap method is called), the observers are notified, and will then perform their corresponding tasks (in this case, exploding the map). Since the data (map) is passed directly into the observer's update method, the data is passed by 'pushing'. Since the subject can store multiple observers and update each of them, this is a one-to-many dependence, with the classes still being loosely coupled (as the observer pattern expects).

### c) Inheritance Design

[Links to your merge requests](/put/links/here)

> i. Name the code smell present in the above code. Identify all subclasses of Entity which have similar code smells that point towards the same root cause.

The code smell in this inheritance structure is called Refused Bequest and involves certain methods or attributes of a parent class not being utilised by a sub class. The smell in the given code is inherited void methods that have no functionality in sub classes.

> ii. Redesign the inheritance structure to solve the problem, in doing so remove the smells.

We replaced all abstract methods that were not utilized by every sub class with a method that has a basic functionality which will be overridden by subclasses which do utilize it.

### d) More Code Smells

[Links to your merge requests](/put/links/here)

> i. What design smell is present in the above description?

The code smell present is shotgun surgery - making a modification to the way items are picked up required the modification of many other classes (the collectables).

> ii. Refactor the code to resolve the smell and underlying problem causing it.

moved the logic for onOverlap into Player, and each instance of overlapping in a Collectable would call the onOverlap method in Player.

### e) Open-Closed Goals

[Links to your merge requests](/put/links/here)

> i. Do you think the design is of good quality here? Do you think it complies with the open-closed principle? Do you think the design should be changed?

[Answer]

> ii. If you think the design is sufficient as it is, justify your decision. If you think the answer is no, pick a suitable Design Pattern that would improve the quality of the code and refactor the code accordingly.

[Briefly explain what you did]

### f) Open Refactoring

[Merge Request 1](/put/links/here)

[Briefly explain what you did]

[Merge Request 2](/put/links/here)

[Briefly explain what you did]

Add all other changes you made in the same format here:

## Task 2) Evolution of Requirements 👽

### a) Microevolution - Enemy Goal

[Links to your merge requests](/put/links/here)

**Assumptions**

[Any assumptions made]

**Design**

[Design]

**Changes after review**

[Design review/Changes made]

**Test list**

[Test List]

**Other notes**

[Any other notes]

### Choice 1 (Insert choice)

[Links to your merge requests](https://nw-syd-gitlab.cseunsw.tech/COMP2511/23T3/teams/T17A_EEVEE/assignment-ii/-/merge_requests/10)

**Assumptions**

[Any assumptions made]

**Design**

[Design]

**Changes after review**

[Design review/Changes made]

**Test list**

[Test List]

**Other notes**

[Any other notes]

### Choice 2 (Snakes)

[Links to your merge requests](https://nw-syd-gitlab.cseunsw.tech/COMP2511/23T3/teams/T17A_EEVEE/assignment-ii/-/merge_requests/10)

**Assumptions**

- Snakes would operate like an enemy.
- There would be inheritance between SnakeHead and SnakeBody.
- The would be recursion used for methods applying to all snake parts.

**Design**

SnakeHead and SnakeBody class were implemented. Snake head is a subclass of snakebody as it can use all methods and attributes that SnakeBody would need. A need method using Djikstra path finding was implemented, returning the distance to the nearest entity that the snake can consume. Snakebody has a SnakeBody attribute for a connecting part and many methods are called recursively to apply to all connected parts. a switch statement was used for each consumable item.

**Changes after review**

**Test list**


- Basic Snake test
- Snake movement
- Snake grow
- Snake two consumables
- Many Consumables
- Simple Battle
- Battle body
- Invincibility Test

**Other notes**

[Any other notes]

### Choice 3 (Insert choice) (If you have a 3rd member)

[Links to your merge requests](/put/links/here)

**Assumptions**

[Any assumptions made]

**Design**

[Design]

**Changes after review**

[Design review/Changes made]

**Test list**

[Test List]

**Other notes**

[Any other notes]

## Task 3) Investigation Task ⁉️

[Merge Request 1](/put/links/here)

[Briefly explain what you did]

[Merge Request 2](/put/links/here)

[Briefly explain what you did]

Add all other changes you made in the same format here:
