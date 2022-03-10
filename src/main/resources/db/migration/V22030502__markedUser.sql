UPDATE users SET marked = false WHERE marked is null;

ALTER TABLE users
  ALTER COLUMN marked SET NOT NULL;