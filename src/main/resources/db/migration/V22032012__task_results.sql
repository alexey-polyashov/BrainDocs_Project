--TABLES

--RESULTS
ALTER TABLE task_results
    ADD result_type integer; --1-positive, 2-withComments, 3-negative, 4-other

UPDATE task_results SET result_type = 1 WHERE id in (1,3,6,8);
UPDATE task_results SET result_type = 3 WHERE id in (2,5,7);
UPDATE task_results SET result_type = 2 WHERE id in (4);

ALTER TABLE task_results
    ALTER COLUMN result_type SET NOT NULL; --1-positive, 2-withComments, 3-negative, 4-other
