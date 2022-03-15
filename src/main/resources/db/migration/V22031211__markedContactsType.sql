UPDATE contacts_types SET marked = false WHERE marked is null;

ALTER TABLE contacts_types
  ALTER COLUMN marked SET NOT NULL;