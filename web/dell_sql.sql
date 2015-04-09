DROP TABLE proofs;
DROP TABLE poes;
DROP TABLE messages;
DROP TABLE projects;
DROP TABLE users;
DROP TABLE companies;
DROP TABLE budgets;

create table companies (
  id NUMBER(5),
  name VARCHAR2(32),
  image_filename VARCHAR2(64),

  PRIMARY KEY (id)
);

SELECT * FROM companies;

create table users (
  id NUMBER(5),
  name VARCHAR2(32),
  role VARCHAR2(20),
  email VARCHAR2(32),
  password VARCHAR2(32),
  company_id int,

  PRIMARY KEY (id),
  FOREIGN KEY (company_id) REFERENCES companies
);

create table budgets (
  quarter DATE,
  amount NUMBER(19),
  available NUMBER(19),

  PRIMARY KEY (quarter)
);

create table projects (
  id NUMBER(5),
  start_time TIMESTAMP,
  end_time TIMESTAMP,
  company_id NUMBER,
  owner_id NUMBER,
  status VARCHAR2(20),
  budget NUMBER(19),
  body VARCHAR2(1024),
  execution_date DATE,
  last_change_admin TIMESTAMP,
  last_change_partner TIMESTAMP,
  unread_admin NUMBER(1,0),
  unread_partner NUMBER(1,0),

  PRIMARY KEY (id),
  FOREIGN KEY (company_id) REFERENCES companies,
  FOREIGN KEY (owner_id) REFERENCES users
);

create table poes (
  id NUMBER(5),
  creation_date TIMESTAMP,
  owner_id NUMBER,
  project_id NUMBER,

  PRIMARY KEY (id),
  FOREIGN KEY (owner_id) REFERENCES users,
  FOREIGN KEY (project_id) REFERENCES projects
);

create table proofs (
  id NUMBER(5),
  poe_id NUMBER,
  filename VARCHAR2(64),
  name VARCHAR2(32),

  PRIMARY KEY (id),
  FOREIGN KEY (poe_id) REFERENCES poes
);

create table messages (
  id NUMBER(5),
  author_id NUMBER,
  project_id NUMBER,
  body VARCHAR2(512),
  creation_time TIMESTAMP,

  PRIMARY KEY (id),
  FOREIGN KEY (author_id) REFERENCES users,
  FOREIGN KEY (project_id) REFERENCES projects
);