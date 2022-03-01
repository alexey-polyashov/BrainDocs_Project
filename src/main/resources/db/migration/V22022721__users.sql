ALTER TABLE users
  ADD confirmed boolean DEFAULT false;

UPDATE users SET confirmed = true;