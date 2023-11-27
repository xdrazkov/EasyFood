--
-- Recipe table definition
--
CREATE TABLE IF NOT EXISTS "Recipe"
(
    `guid`      VARCHAR     NOT NULL UNIQUE,
    `title`    VARCHAR(150) NOT NULL,
    `description`      VARCHAR(150) NOT NULL,
    `portionCount`      INT,
    `instructions`      VARCHAR(150) NOT NULL,
    `timeToPrepare`      INT,
    `category`      VARCHAR REFERENCES "Category"(`guid`),
);

--
-- Category table definition
--
CREATE TABLE IF NOT EXISTS "Category"
(
    `guid`      VARCHAR     NOT NULL UNIQUE,
    `name`    VARCHAR(150) NOT NULL,
    `color`    INT,
);

--
-- Unit table definition
--
CREATE TABLE IF NOT EXISTS "Unit"
(
    `guid`      VARCHAR     NOT NULL UNIQUE,
    `name`    VARCHAR(150) NOT NULL,
    `abbreviation`    VARCHAR(150) NOT NULL,
    `ingredientType`    VARCHAR(150) NOT NULL,
    `conversionRate`    FLOAT,
);


---
--- Insert base units
---
INSERT INTO "Unit" ("guid", "name", "abbreviation", "ingredientType", "conversionRate")
VALUES ('5b22b51c-3b9e-4bd4-8140-2db618ece49b', 'gram', 'g', 'WEIGHABLE', 1.0),
       ('32d9b80f-48c1-4aa1-8903-9c0b306ab454', 'milliliter', 'ml', 'POURABLE', 1.0),
       ('2f4c2180-0d1c-4299-bcdb-a749126b0537', 'piece', 'pc', 'COUNTABLE', 1.0)
;


--
-- RecipeIngredient table definition
--
CREATE TABLE IF NOT EXISTS "RecipeIngredient"
(
    `recipe`    VARCHAR REFERENCES "Recipe"(`guid`),
    `ingredient`    VARCHAR REFERENCES "Ingredient"(`guid`),
    `unit`    VARCHAR REFERENCES "Unit"(`guid`),
    `amount`    INT,
);
