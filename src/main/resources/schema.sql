
DROP TABLE IF EXISTS userprofiles_tours;
DROP TABLE IF EXISTS userprofiles_pois;
DROP TABLE IF EXISTS tours_pois;
DROP TABLE IF EXISTS userprofile;
DROP TABLE IF EXISTS tour;
DROP TABLE IF EXISTS poi;
DROP TABLE IF EXISTS webprofile;

CREATE TABLE userprofile (
	username VARCHAR(20),
	userpass VARCHAR(20),
	PRIMARY KEY (username)
);

CREATE TABLE webprofile (
  username VARCHAR(20),
  userpass VARCHAR(20),

  PRIMARY KEY (userName)
);

CREATE TABLE tour (
	tourid INT AUTO_INCREMENT,
	tourname VARCHAR(255),
	owner VARCHAR(20),

	PRIMARY KEY (tourid),
	FOREIGN KEY (owner) REFERENCES webprofile(username)
);

CREATE TABLE poi (
	beaconid INT,
	lat DOUBLE PRECISION,
	lon DOUBLE PRECISION,
	description VARCHAR(1000),
	imageurl VARCHAR(100),
	name VARCHAR(255),
	owner VARCHAR(20),

	PRIMARY KEY (beaconid),
	FOREIGN KEY (owner) REFERENCES webprofile(username)
);

CREATE TABLE userprofiles_tours (
	username VARCHAR(20),
	tourid INT,

	PRIMARY KEY (username, tourid),
	FOREIGN KEY (username) REFERENCES userprofile(username),
	FOREIGN KEY (tourid) REFERENCES tour(tourid)
);

CREATE TABLE userprofiles_pois (
	username VARCHAR(20),
	beaconid INT,

	PRIMARY KEY (username, beaconid),
	FOREIGN KEY (username) REFERENCES userprofile(username),
	FOREIGN KEY (beaconid) REFERENCES poi(beaconid)
);

CREATE TABLE tours_pois (
	tourid INT,
	beaconid INT,

	PRIMARY KEY (tourid, beaconid),
	FOREIGN KEY (tourid) REFERENCES tour(tourid),
	FOREIGN KEY (beaconid) REFERENCES poi(beaconid)
);



