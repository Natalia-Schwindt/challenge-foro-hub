ALTER TABLE usuarios ADD nombre VARCHAR(100);
ALTER TABLE usuarios ADD email VARCHAR(100);
ALTER TABLE usuarios ADD CONSTRAINT unique_email UNIQUE (email);