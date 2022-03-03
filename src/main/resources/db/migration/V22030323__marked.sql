UPDATE documents SET marked = false WHERE marked is null;
UPDATE documents_types SET marked = false WHERE marked is null;
UPDATE organisations SET marked = false WHERE marked is null;

ALTER TABLE documents
  ALTER COLUMN marked SET NOT NULL;

ALTER TABLE documents_types
  ALTER COLUMN marked SET NOT NULL;

ALTER TABLE organisations
    ALTER COLUMN marked SET NOT NULL;