--    Upsources POS is a point of sales application designed for touch screens.
--    Copyright (C) 2007-2011 Openbravo, S.L.
--    http://sourceforge.net/projects/openbravopos
--
--    This file is part of Upsources POS.
--
--    Upsources POS is free software: you can redistribute it and/or modify
--    it under the terms of the GNU General Public License as published by
--    the Free Software Foundation, either version 3 of the License, or
--    (at your option) any later version.
--
--    Upsources POS is distributed in the hope that it will be useful,
--    but WITHOUT ANY WARRANTY; without even the implied warranty of
--    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
--    GNU General Public License for more details.
--
--    You should have received a copy of the GNU General Public License
--    along with Upsources POS.  If not, see <http://www.gnu.org/licenses/>.

-- Database initial script for ORACLE
-- v2.30.2

CREATE TABLE APPLICATIONS (
    ID VARCHAR2(256) NOT NULL,
    NAME VARCHAR2(1024) NOT NULL,
    VERSION VARCHAR2(1024) NOT NULL,
    PRIMARY KEY (ID)
);
INSERT INTO APPLICATIONS(ID, NAME, VERSION) VALUES($APP_ID{}, $APP_NAME{}, $APP_VERSION{});

CREATE TABLE ROLES (
    ID VARCHAR2(256) NOT NULL,
    NAME VARCHAR2(1024) NOT NULL,
    PERMISSIONS BLOB,
    PRIMARY KEY (ID)
);
CREATE UNIQUE INDEX ROLES_NAME_INX ON ROLES(NAME);
INSERT INTO ROLES(ID, NAME, PERMISSIONS) VALUES('0', 'Administrator role', $FILE{/com/openbravo/pos/templates/Role.Administrator.xml} );
INSERT INTO ROLES(ID, NAME, PERMISSIONS) VALUES('1', 'Manager role', $FILE{/com/openbravo/pos/templates/Role.Manager.xml} );
INSERT INTO ROLES(ID, NAME, PERMISSIONS) VALUES('2', 'Employee role', $FILE{/com/openbravo/pos/templates/Role.Employee.xml} );
INSERT INTO ROLES(ID, NAME, PERMISSIONS) VALUES('3', 'Guest role', $FILE{/com/openbravo/pos/templates/Role.Guest.xml} );

CREATE TABLE PEOPLE (
    ID VARCHAR2(256) NOT NULL,
    NAME VARCHAR2(1024) NOT NULL,
    APPPASSWORD VARCHAR2(1024),
    CARD VARCHAR2(1024),
    ROLE VARCHAR2(256) NOT NULL,
    VISIBLE NUMERIC(1) NOT NULL,
    IMAGE BLOB,
    PRIMARY KEY (ID),
    CONSTRAINT PEOPLE_FK_1 FOREIGN KEY (ROLE) REFERENCES ROLES(ID)
);
CREATE UNIQUE INDEX PEOPLE_NAME_INX ON PEOPLE(NAME);
CREATE INDEX PEOPLE_CARD_INX ON PEOPLE(CARD);

INSERT INTO PEOPLE(ID, NAME, APPPASSWORD, ROLE, VISIBLE, IMAGE) VALUES ('0', 'Administrator', NULL, '0', 1, NULL);
INSERT INTO PEOPLE(ID, NAME, APPPASSWORD, ROLE, VISIBLE, IMAGE) VALUES ('1', 'Manager', NULL, '1', 1, NULL);
INSERT INTO PEOPLE(ID, NAME, APPPASSWORD, ROLE, VISIBLE, IMAGE) VALUES ('2', 'Employee', NULL, '2', 1, NULL);
INSERT INTO PEOPLE(ID, NAME, APPPASSWORD, ROLE, VISIBLE, IMAGE) VALUES ('3', 'Guest', NULL, '3', 1, NULL);

CREATE TABLE RESOURCES (
    ID VARCHAR2(256) NOT NULL,
    NAME VARCHAR2(1024) NOT NULL,
    RESTYPE INTEGER NOT NULL,
    CONTENT BLOB,
    PRIMARY KEY (ID)
);
CREATE UNIQUE INDEX RESOURCES_NAME_INX ON RESOURCES(NAME);
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('0', 'Printer.Start', 0, $FILE{/com/openbravo/pos/templates/Printer.Start.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('1', 'Printer.Ticket', 0, $FILE{/com/openbravo/pos/templates/Printer.Ticket.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('2', 'Printer.Ticket2', 0, $FILE{/com/openbravo/pos/templates/Printer.Ticket2.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('3', 'Printer.TicketPreview', 0, $FILE{/com/openbravo/pos/templates/Printer.TicketPreview.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('4', 'Printer.TicketTotal', 0, $FILE{/com/openbravo/pos/templates/Printer.TicketTotal.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('5', 'Printer.OpenDrawer', 0, $FILE{/com/openbravo/pos/templates/Printer.OpenDrawer.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('6', 'Printer.Ticket.Logo', 1, $FILE{/com/openbravo/pos/templates/Printer.Ticket.Logo.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('7', 'Printer.TicketLine', 0, $FILE{/com/openbravo/pos/templates/Printer.TicketLine.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('8', 'Printer.CloseCash', 0, $FILE{/com/openbravo/pos/templates/Printer.CloseCash.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('9', 'Window.Logo', 1, $FILE{/com/openbravo/pos/templates/Window.Logo.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('10', 'Window.Title', 0, $FILE{/com/openbravo/pos/templates/Window.Title.txt});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('11', 'Ticket.Buttons', 0, $FILE{/com/openbravo/pos/templates/Ticket.Buttons.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('12', 'Ticket.Line', 0, $FILE{/com/openbravo/pos/templates/Ticket.Line.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('13', 'Printer.Inventory', 0, $FILE{/com/openbravo/pos/templates/Printer.Inventory.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('14', 'Menu.Root', 0, $FILE{/com/openbravo/pos/templates/Menu.Root.txt});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('15', 'Printer.CustomerPaid', 0, $FILE{/com/openbravo/pos/templates/Printer.CustomerPaid.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('16', 'Printer.CustomerPaid2', 0, $FILE{/com/openbravo/pos/templates/Printer.CustomerPaid2.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('17', 'payment.cash', 0, $FILE{/com/openbravo/pos/templates/payment.cash.txt});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('18', 'banknote.50euro', 1, $FILE{/com/openbravo/pos/templates/banknote.50euro.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('19', 'banknote.20euro', 1, $FILE{/com/openbravo/pos/templates/banknote.20euro.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('20', 'banknote.10euro', 1, $FILE{/com/openbravo/pos/templates/banknote.10euro.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('21', 'banknote.5euro', 1, $FILE{/com/openbravo/pos/templates/banknote.5euro.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('22', 'coin.2euro', 1, $FILE{/com/openbravo/pos/templates/coin.2euro.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('23', 'coin.1euro', 1, $FILE{/com/openbravo/pos/templates/coin.1euro.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('24', 'coin.50cent', 1, $FILE{/com/openbravo/pos/templates/coin.50cent.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('25', 'coin.20cent', 1, $FILE{/com/openbravo/pos/templates/coin.20cent.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('26', 'coin.10cent', 1, $FILE{/com/openbravo/pos/templates/coin.10cent.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('27', 'coin.5cent', 1, $FILE{/com/openbravo/pos/templates/coin.5cent.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('28', 'coin.2cent', 1, $FILE{/com/openbravo/pos/templates/coin.2cent.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('29', 'coin.1cent', 1, $FILE{/com/openbravo/pos/templates/coin.1cent.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('30', 'Printer.PartialCash', 0, $FILE{/com/openbravo/pos/templates/Printer.PartialCash.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('31', 'Printer.SupplierPaid', 0, $FILE{/com/openbravo/pos/templates/Printer.SupplierPaid.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('32', 'Printer.TicketBar', 0, $FILE{/com/openbravo/pos/templates/Printer.TicketBar.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('33', 'Printer.TicketChange_Bar', 0, $FILE{/com/openbravo/pos/templates/Printer.TicketChange_Bar.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('34', 'Printer.TicketChange_Kitchen', 0, $FILE{/com/openbravo/pos/templates/Printer.TicketChange_Kitchen.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('35', 'Printer.TicketKitchen', 0, $FILE{/com/openbravo/pos/templates/Printer.TicketKitchen.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('36', 'event.removeline', 0, $FILE{/com/openbravo/pos/templates/event.removeline.script});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('37', 'event.setline', 0, $FILE{/com/openbravo/pos/templates/event.setline.script});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('38', 'event.total', 0, $FILE{/com/openbravo/pos/templates/event.total.script});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('39', 'sendorder', 0, $FILE{/com/openbravo/pos/templates/sendorder.script});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('40', 'Printer.TicketFiscal', 0, $FILE{/com/openbravo/pos/templates/Printer.TicketFiscal.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('41', 'Printer.TicketFPrint', 0, $FILE{/com/openbravo/pos/templates/Printer.TicketFPrint.xml});

CREATE TABLE TAXCUSTCATEGORIES (
    ID VARCHAR2(256) NOT NULL,
    NAME VARCHAR2(1024) NOT NULL,
    PRIMARY KEY (ID)
);
CREATE UNIQUE INDEX TAXCUSTCAT_NAME_INX ON TAXCUSTCATEGORIES(NAME);

CREATE TABLE CUSTOMERS (
    ID VARCHAR2(256) NOT NULL,
    SEARCHKEY VARCHAR2(1024) NOT NULL,
    TAXID VARCHAR2(1024),
    NAME VARCHAR2(1024) NOT NULL,
    TAXCATEGORY VARCHAR2(256),
    CARD VARCHAR2(1024),
    MAXDEBT DOUBLE PRECISION DEFAULT 0 NOT NULL,
    ADDRESS VARCHAR2(1024),
    ADDRESS2 VARCHAR2(1024),
    POSTAL VARCHAR2(1024),
    CITY VARCHAR2(1024),
    REGION VARCHAR2(1024),
    COUNTRY VARCHAR2(1024),
    SHIPPINGADDRESS VARCHAR2(1024),
    SHIPPINGADDRESS2 VARCHAR2(1024),
    SHIPPINGPOSTAL VARCHAR2(1024),
    SHIPPINGCITY VARCHAR2(1024),
    SHIPPINGREGION VARCHAR2(1024),
    SHIPPINGCOUNTRY VARCHAR2(1024),
    FIRSTNAME VARCHAR2(1024),
    LASTNAME VARCHAR2(1024),
    EMAIL VARCHAR2(1024),
    PHONE VARCHAR2(1024),
    PHONE2 VARCHAR2(1024),
    FAX VARCHAR2(1024),
    NOTES VARCHAR2(1024),
    VISIBLE NUMERIC(1) DEFAULT 1 NOT NULL,
    CURDATE TIMESTAMP,
    CURDEBT DOUBLE PRECISION,
    DEBTDATELIMIT VARCHAR2(1024),
    DEBTMODE VARCHAR2(1024),
    COMPANYNAME VARCHAR2(256),
    CUI VARCHAR2(256),
    NRREG VARCHAR2(256),
    COMPANYNOTE VARCHAR2(256),
    PRIMARY KEY (ID),
    CONSTRAINT CUSTOMERS_TAXCAT FOREIGN KEY (TAXCATEGORY) REFERENCES TAXCUSTCATEGORIES(ID)
);
CREATE UNIQUE INDEX CUSTOMERS_SKEY_INX ON CUSTOMERS(SEARCHKEY);
CREATE INDEX CUSTOMERS_TAXID_INX ON CUSTOMERS(TAXID);
CREATE INDEX CUSTOMERS_NAME_INX ON CUSTOMERS(NAME);
CREATE INDEX CUSTOMERS_CARD_INX ON CUSTOMERS(CARD);

CREATE TABLE CATEGORIES (
    ID VARCHAR2(256) NOT NULL,
    NAME VARCHAR2(1024) NOT NULL,
    PARENTID VARCHAR2(256),
    IMAGE BLOB,
    STARTHOUR TIME DEFAULT '00:00:00',
    ENDHOUR TIME DEFAULT '23:59:59',
    PRIMARY KEY(ID),
    CONSTRAINT CATEGORIES_FK_1 FOREIGN KEY (PARENTID) REFERENCES CATEGORIES(ID)
);
CREATE UNIQUE INDEX CATEGORIES_NAME_INX ON CATEGORIES(NAME);
INSERT INTO TAXCATEGORIES(ID, NAME) VALUES ('000', 'Tax Exempt');
INSERT INTO TAXCATEGORIES(ID, NAME) VALUES ('001', 'Tax Standard');
INSERT INTO CATEGORIES(ID, NAME) VALUES ('-1', 'BOM');

CREATE TABLE TAXCATEGORIES (
    ID VARCHAR2(256) NOT NULL,
    NAME VARCHAR2(1024) NOT NULL,
    PRIMARY KEY (ID)
);
CREATE UNIQUE INDEX TAXCAT_NAME_INX ON TAXCATEGORIES(NAME);
INSERT INTO TAXCATEGORIES(ID, NAME) VALUES ('000', 'Reduzido');
INSERT INTO TAXCATEGORIES(ID, NAME) VALUES ('001', 'Intermédio');
INSERT INTO TAXCATEGORIES(ID, NAME) VALUES ('002', 'Normal');
INSERT INTO TAXCATEGORIES(ID, NAME) VALUES ('003', 'Isento');
INSERT INTO TAXCATEGORIES(ID, NAME) VALUES ('004', 'Outros');
INSERT INTO TAXCATEGORIES(ID, NAME) VALUES ('-1', 'BOM');

CREATE TABLE TAXES (
    ID VARCHAR2(256) NOT NULL,
    NAME VARCHAR2(1024) NOT NULL,
    VALIDFROM TIMESTAMP DEFAULT TO_DATE('2000-01-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS') NOT NULL,
    CATEGORY VARCHAR2(256) NOT NULL,
    CUSTCATEGORY VARCHAR2(256),
    PARENTID VARCHAR2(256),
    RATE DOUBLE PRECISION NOT NULL,
    RATECASCADE NUMERIC(1) DEFAULT 0 NOT NULL,
    RATEORDER INTEGER,
    PRIMARY KEY(ID),
    CONSTRAINT TAXES_CAT_FK FOREIGN KEY (CATEGORY) REFERENCES TAXCATEGORIES(ID),
    CONSTRAINT TAXES_CUSTCAT_FK FOREIGN KEY (CUSTCATEGORY) REFERENCES TAXCUSTCATEGORIES(ID),
    CONSTRAINT TAXES_TAXES_FK FOREIGN KEY (PARENTID) REFERENCES TAXES(ID)
);
CREATE UNIQUE INDEX TAXES_NAME_INX ON TAXES(NAME);
INSERT INTO TAXES(ID, NAME, CATEGORY, CUSTCATEGORY, PARENTID, RATE, RATECASCADE, RATEORDER) VALUES ('000', 'Tax Exempt', '000', NULL, NULL, 0, 0, NULL);
INSERT INTO TAXES(ID, NAME, CATEGORY, CUSTCATEGORY, PARENTID, RATE, RATECASCADE, RATEORDER) VALUES ('001', 'Tax Standard', '001', NULL, NULL, 0.10, 0, NULL);
INSERT INTO TAXES(ID, NAME, CATEGORY, CUSTCATEGORY, PARENTID, RATE, RATECASCADE, RATEORDER) VALUES ('-1', 'BOM', '-1', NULL, NULL, 0.0, FALSE, NULL);

CREATE TABLE ATTRIBUTE (
    ID VARCHAR2(256) NOT NULL,
    NAME VARCHAR2(1024) NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE ATTRIBUTEVALUE (
    ID VARCHAR2(256) NOT NULL,
    ATTRIBUTE_ID VARCHAR2(256) NOT NULL,
    VALUE VARCHAR2(1024),
    PRIMARY KEY (ID),
    CONSTRAINT ATTVAL_ATT FOREIGN KEY (ATTRIBUTE_ID) REFERENCES ATTRIBUTE(ID) ON DELETE CASCADE
);

CREATE TABLE ATTRIBUTESET (
    ID VARCHAR2(256) NOT NULL,
    NAME VARCHAR2(1024) NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE ATTRIBUTEUSE (
    ID VARCHAR2(256) NOT NULL,
    ATTRIBUTESET_ID VARCHAR2(256) NOT NULL,
    ATTRIBUTE_ID VARCHAR2(256) NOT NULL,
    LINENO INTEGER,
    PRIMARY KEY (ID),
    CONSTRAINT ATTUSE_SET FOREIGN KEY (ATTRIBUTESET_ID) REFERENCES ATTRIBUTESET(ID) ON DELETE CASCADE,
    CONSTRAINT ATTUSE_ATT FOREIGN KEY (ATTRIBUTE_ID) REFERENCES ATTRIBUTE(ID)
);
CREATE UNIQUE INDEX ATTUSE_LINE ON ATTRIBUTEUSE(ATTRIBUTESET_ID, LINENO);

CREATE TABLE ATTRIBUTESETINSTANCE (
    ID VARCHAR2(256) NOT NULL,
    ATTRIBUTESET_ID VARCHAR2(256) NOT NULL,
    DESCRIPTION VARCHAR2(1024),
    PRIMARY KEY (ID),
    CONSTRAINT ATTSETINST_SET FOREIGN KEY (ATTRIBUTESET_ID) REFERENCES ATTRIBUTESET(ID) ON DELETE CASCADE
);

CREATE TABLE ATTRIBUTEINSTANCE (
    ID VARCHAR2(256) NOT NULL,
    ATTRIBUTESETINSTANCE_ID VARCHAR2(256) NOT NULL,
    ATTRIBUTE_ID VARCHAR2(256) NOT NULL,
    VALUE VARCHAR2(1024),
    PRIMARY KEY (ID),
    CONSTRAINT ATTINST_SET FOREIGN KEY (ATTRIBUTESETINSTANCE_ID) REFERENCES ATTRIBUTESETINSTANCE(ID) ON DELETE CASCADE,
    CONSTRAINT ATTINST_ATT FOREIGN KEY (ATTRIBUTE_ID) REFERENCES ATTRIBUTE(ID)
);

CREATE TABLE PRODUCTS (
    ID VARCHAR2(256) NOT NULL,
    REFERENCE VARCHAR2(1024) NOT NULL,
    CODE VARCHAR2(1024) NOT NULL,
    UNIT VARCHAR2(1024),
    CODETYPE VARCHAR2(1024),
    NAME VARCHAR2(1024) NOT NULL,
    PRICEBUY DOUBLE PRECISION NOT NULL,
    PRICESELL DOUBLE PRECISION NOT NULL,
    CATEGORY VARCHAR2(256) NOT NULL,
    TAXCAT VARCHAR2(256) NOT NULL,
    ATTRIBUTESET_ID VARCHAR2(256),
    STOCKCOST DOUBLE PRECISION,
    STOCKVOLUME DOUBLE PRECISION,
    IMAGE BLOB,
    ISCOM  NUMERIC(1) DEFAULT 0 NOT NULL,
    ISSCALE  NUMERIC(1) DEFAULT 0 NOT NULL,
    ATTRIBUTES BLOB,
    PRIMARY KEY (ID),
    CONSTRAINT PRODUCTS_FK_1 FOREIGN KEY (CATEGORY) REFERENCES CATEGORIES(ID),
    CONSTRAINT PRODUCTS_TAXCAT_FK FOREIGN KEY (TAXCAT) REFERENCES TAXCATEGORIES(ID),
    CONSTRAINT PRODUCTS_ATTRSET_FK FOREIGN KEY (ATTRIBUTESET_ID) REFERENCES ATTRIBUTESET(ID)
);
CREATE UNIQUE INDEX PRODUCTS_INX_0 ON PRODUCTS(REFERENCE);
CREATE UNIQUE INDEX PRODUCTS_INX_1 ON PRODUCTS(CODE);
CREATE UNIQUE INDEX PRODUCTS_NAME_INX ON PRODUCTS(NAME);

CREATE TABLE PRODUCTS_CAT (
    PRODUCT VARCHAR2(256) NOT NULL,
    CATORDER INTEGER,
    PRIMARY KEY (PRODUCT),
    CONSTRAINT PRODUCTS_CAT_FK_1 FOREIGN KEY (PRODUCT) REFERENCES PRODUCTS(ID)
);
CREATE INDEX PRODUCTS_CAT_INX_1 ON PRODUCTS_CAT(CATORDER);

CREATE TABLE PRODUCTS_COM (
    ID VARCHAR2(256) NOT NULL,
    PRODUCT VARCHAR2(256) NOT NULL,
    PRODUCT2 VARCHAR2(256) NOT NULL,
    PRIMARY KEY (ID),
    CONSTRAINT PRODUCTS_COM_FK_1 FOREIGN KEY (PRODUCT) REFERENCES PRODUCTS(ID),
    CONSTRAINT PRODUCTS_COM_FK_2 FOREIGN KEY (PRODUCT2) REFERENCES PRODUCTS(ID)
);
CREATE UNIQUE INDEX PCOM_INX_PROD ON PRODUCTS_COM(PRODUCT, PRODUCT2);

CREATE TABLE PRODUCTS_MAT (
    PRODUCT VARCHAR2(255) NOT NULL,
    MATERIAL VARCHAR2(255) NOT NULL,
    AMOUNT DOUBLE PRECISION NOT NULL,
    PRIMARY KEY (PRODUCT, MATERIAL),
    CONSTRAINT PROD_MAT_FK_1 FOREIGN KEY (PRODUCT) REFERENCES PRODUCTS(ID),
    CONSTRAINT PROD_MAT_FK_2 FOREIGN KEY (MATERIAL) REFERENCES PRODUCTS(ID)
);

CREATE TABLE SUBGROUPS (
    ID VARCHAR2(255) NOT NULL,
    COMPOSITION VARCHAR2(255) NOT NULL,
    NAME VARCHAR2(255) NOT NULL,
    IMAGE BLOB,
    PRIMARY KEY(ID),
    CONSTRAINT SUBGROUPS_FK_1 FOREIGN KEY (COMPOSITION) REFERENCES PRODUCTS(ID) ON DELETE CASCADE
);

CREATE TABLE SUBGROUPS_PROD (
    SUBGROUP VARCHAR2(255) NOT NULL,
    PRODUCT VARCHAR2(255) NOT NULL,
    PRIMARY KEY (SUBGROUP, PRODUCT),
    CONSTRAINT SUBGROUPS_PROD_FK_1 FOREIGN KEY (SUBGROUP) REFERENCES SUBGROUPS(ID) ON DELETE CASCADE,
    CONSTRAINT SUBGROUPS_PROD_FK_2 FOREIGN KEY (PRODUCT) REFERENCES PRODUCTS(ID) ON DELETE CASCADE
);

CREATE TABLE LOCATIONS (
    ID VARCHAR2(256) NOT NULL,
    NAME VARCHAR2(1024) NOT NULL,
    ADDRESS VARCHAR2(1024),
    PRIMARY KEY (ID)
);
CREATE UNIQUE INDEX LOCATIONS_NAME_INX ON LOCATIONS(NAME);
INSERT INTO LOCATIONS(ID, NAME,ADDRESS) VALUES('0', 'General', NULL);

CREATE TABLE STOCKDIARY (
    ID VARCHAR2(256) NOT NULL,
    DATENEW TIMESTAMP NOT NULL,
    REASON INTEGER NOT NULL,
    LOCATION VARCHAR2(256) NOT NULL,
    PRODUCT VARCHAR2(256) NOT NULL,
    ATTRIBUTESETINSTANCE_ID VARCHAR2(256),
    UNITS DOUBLE PRECISION NOT NULL,
    PRICE DOUBLE PRECISION NOT NULL,
    SUPPLIERID VARCHAR2(256),
    PRIMARY KEY (ID),
    CONSTRAINT STOCKDIARY_FK_1 FOREIGN KEY (PRODUCT) REFERENCES PRODUCTS(ID),
    CONSTRAINT STOCKDIARY_ATTSETINST FOREIGN KEY (ATTRIBUTESETINSTANCE_ID) REFERENCES ATTRIBUTESETINSTANCE(ID),
    CONSTRAINT STOCKDIARY_FK_2 FOREIGN KEY (LOCATION) REFERENCES LOCATIONS(ID)
);
CREATE INDEX STOCKDIARY_INX_1 ON STOCKDIARY(DATENEW);

CREATE TABLE STOCKLEVEL (
    ID VARCHAR2(256) NOT NULL,
    LOCATION VARCHAR2(256) NOT NULL,
    PRODUCT VARCHAR2(256) NOT NULL,
    STOCKSECURITY DOUBLE PRECISION,
    STOCKMAXIMUM DOUBLE PRECISION,
    PRIMARY KEY (ID),
    CONSTRAINT STOCKLEVEL_PRODUCT FOREIGN KEY (PRODUCT) REFERENCES PRODUCTS(ID),
    CONSTRAINT STOCKLEVEL_LOCATION FOREIGN KEY (LOCATION) REFERENCES LOCATIONS(ID)
);

CREATE TABLE STOCKCURRENT (
    LOCATION VARCHAR2(256) NOT NULL,
    PRODUCT VARCHAR2(256) NOT NULL,
    ATTRIBUTESETINSTANCE_ID VARCHAR2(256),
    UNITS DOUBLE PRECISION NOT NULL,
    CONSTRAINT STOCKCURRENT_FK_1 FOREIGN KEY (PRODUCT) REFERENCES PRODUCTS(ID),
    CONSTRAINT STOCKCURRENT_ATTSETINST FOREIGN KEY (ATTRIBUTESETINSTANCE_ID) REFERENCES ATTRIBUTESETINSTANCE(ID),
    CONSTRAINT STOCKCURRENT_FK_2 FOREIGN KEY (LOCATION) REFERENCES LOCATIONS(ID)
);
CREATE UNIQUE INDEX STOCKCURRENT_INX ON STOCKCURRENT(LOCATION, PRODUCT, ATTRIBUTESETINSTANCE_ID);

CREATE TABLE CLOSEDCASH (
    MONEY VARCHAR2(256) NOT NULL,
    HOST VARCHAR2(1024) NOT NULL,
    HOSTSEQUENCE INTEGER NOT NULL,
    DATESTART TIMESTAMP NOT NULL,
    DATEEND TIMESTAMP,
    PRIMARY KEY(MONEY)
);
CREATE INDEX CLOSEDCASH_INX_1 ON CLOSEDCASH(DATESTART);
CREATE UNIQUE INDEX CLOSEDCASH_INX_SEQ ON CLOSEDCASH(HOST, HOSTSEQUENCE);

CREATE TABLE RECEIPTS (
    ID VARCHAR2(256) NOT NULL,
    MONEY VARCHAR2(256) NOT NULL,
    DATENEW TIMESTAMP NOT NULL,
    ATTRIBUTES BLOB,
    PRIMARY KEY(ID),
    CONSTRAINT RECEIPTS_FK_MONEY FOREIGN KEY (MONEY) REFERENCES CLOSEDCASH(MONEY)
);
CREATE INDEX RECEIPTS_INX_1 ON RECEIPTS(DATENEW);

CREATE TABLE TICKETS (
    ID VARCHAR2(256) NOT NULL,
    TICKETTYPE INTEGER DEFAULT 0 NOT NULL,
    TICKETID INTEGER NOT NULL,
    PERSON VARCHAR2(256) NOT NULL,
    CUSTOMER VARCHAR2(256),
    SUPPLIER VARCHAR2(256),
    STATUS INTEGER DEFAULT 0 NOT NULL,
    DUEDATE TIMESTAMP DEFAULT TO_DATE('1990-01-01 09:00:00', 'YYYY-MM-DD HH24:MI:SS') NOT NULL,
    PRIMARY KEY (ID),
    CONSTRAINT TICKETS_FK_ID FOREIGN KEY (ID) REFERENCES RECEIPTS(ID),
    CONSTRAINT TICKETS_FK_2 FOREIGN KEY (PERSON) REFERENCES PEOPLE(ID),
    CONSTRAINT TICKETS_CUSTOMERS_FK FOREIGN KEY (CUSTOMER) REFERENCES CUSTOMERS(ID)
);
CREATE INDEX TICKETS_TICKETID ON TICKETS(TICKETTYPE, TICKETID);

CREATE SEQUENCE TICKETSNUM START WITH 1;
CREATE SEQUENCE TICKETSNUM_REFUND START WITH 1;
CREATE SEQUENCE TICKETSNUM_PAYMENT START WITH 1;
CREATE SEQUENCE TICKETSNUM_SUPPLIER START WITH 1;

CREATE TABLE TICKETLINES (
    TICKET VARCHAR2(256) NOT NULL,
    LINE INTEGER NOT NULL,
    PRODUCT VARCHAR2(256),
    ATTRIBUTESETINSTANCE_ID VARCHAR2(256),
    UNITS DOUBLE PRECISION NOT NULL,
    PRICE DOUBLE PRECISION NOT NULL,
    TAXID VARCHAR2(256) NOT NULL,
    ATTRIBUTES BLOB,
    PRIMARY KEY (TICKET, LINE),
    CONSTRAINT TICKETLINES_FK_TICKET FOREIGN KEY (TICKET) REFERENCES TICKETS(ID),
    CONSTRAINT TICKETLINES_FK_2 FOREIGN KEY (PRODUCT) REFERENCES PRODUCTS(ID),
    CONSTRAINT TICKETLINES_ATTSETINST FOREIGN KEY (ATTRIBUTESETINSTANCE_ID) REFERENCES ATTRIBUTESETINSTANCE(ID),
    CONSTRAINT TICKETLINES_FK_3 FOREIGN KEY (TAXID) REFERENCES TAXES(ID)
);

CREATE TABLE PAYMENTS (
    ID VARCHAR2(256) NOT NULL,
    RECEIPT VARCHAR2(256) NOT NULL,
    PAYMENT VARCHAR2(1024) NOT NULL,
    TOTAL DOUBLE PRECISION NOT NULL,
    TRANSID VARCHAR2(1024),
    RETURNMSG BLOB,
    CHEQUENUMBER VARCHAR2(256),
    CHEQUEDATE VARCHAR2(1024),
    BANKNAME VARCHAR2(1024),
    TICKETID VARCHAR2(1024),
    SUPPLIERID VARCHAR2(1024),
    PRIMARY KEY (ID),
    CONSTRAINT PAYMENTS_FK_RECEIPT FOREIGN KEY (RECEIPT) REFERENCES RECEIPTS(ID)
);
CREATE INDEX PAYMENTS_INX_1 ON PAYMENTS(PAYMENT);

CREATE TABLE TAXLINES (
    ID VARCHAR2(256) NOT NULL,
    RECEIPT VARCHAR2(256) NOT NULL,
    TAXID VARCHAR2(256) NOT NULL,
    BASE DOUBLE PRECISION NOT NULL,
    AMOUNT DOUBLE PRECISION NOT NULL,
    PRIMARY KEY (ID),
    CONSTRAINT TAXLINES_TAX FOREIGN KEY (TAXID) REFERENCES TAXES(ID),
    CONSTRAINT TAXLINES_RECEIPT FOREIGN KEY (RECEIPT) REFERENCES RECEIPTS(ID)
);

CREATE TABLE FLOORS (
    ID VARCHAR2(256) NOT NULL,
    NAME VARCHAR2(1024) NOT NULL,
    IMAGE BLOB,
    PRIMARY KEY (ID)
);
CREATE UNIQUE INDEX FLOORS_NAME_INX ON FLOORS(NAME);
INSERT INTO FLOORS(ID, NAME, IMAGE) VALUES ('0', 'Restaurant floor', $FILE{/com/openbravo/pos/templates/restaurantsample.png});

CREATE TABLE PLACES (
    ID VARCHAR2(256) NOT NULL,
    NAME VARCHAR2(1024) NOT NULL,
    X INTEGER NOT NULL,
    Y INTEGER NOT NULL,
    FLOOR VARCHAR2(256) NOT NULL,
    PRIMARY KEY (ID),
    CONSTRAINT PLACES_FK_1 FOREIGN KEY (FLOOR) REFERENCES FLOORS(ID)
);
CREATE UNIQUE INDEX PLACES_NAME_INX ON PLACES(NAME);
INSERT INTO PLACES(ID, NAME, X, Y, FLOOR) VALUES ('1', 'Table 1', 133, 151, '0');
INSERT INTO PLACES(ID, NAME, X, Y, FLOOR) VALUES ('2', 'Table 2', 532, 151, '0');
INSERT INTO PLACES(ID, NAME, X, Y, FLOOR) VALUES ('3', 'Table 3', 133, 264, '0');
INSERT INTO PLACES(ID, NAME, X, Y, FLOOR) VALUES ('4', 'Table 4', 266, 264, '0');
INSERT INTO PLACES(ID, NAME, X, Y, FLOOR) VALUES ('5', 'Table 5', 399, 264, '0');
INSERT INTO PLACES(ID, NAME, X, Y, FLOOR) VALUES ('6', 'Table 6', 532, 264, '0');
INSERT INTO PLACES(ID, NAME, X, Y, FLOOR) VALUES ('7', 'Table 7', 133, 377, '0');
INSERT INTO PLACES(ID, NAME, X, Y, FLOOR) VALUES ('8', 'Table 8', 266, 377, '0');
INSERT INTO PLACES(ID, NAME, X, Y, FLOOR) VALUES ('9', 'Table 9', 399, 377, '0');
INSERT INTO PLACES(ID, NAME, X, Y, FLOOR) VALUES ('10', 'Table 10', 532, 377, '0');

CREATE TABLE RESERVATIONS (
    ID VARCHAR2(256) NOT NULL,
    CREATED TIMESTAMP NOT NULL,
    DATENEW TIMESTAMP DEFAULT TO_DATE('2001-01-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS') NOT NULL,
    TITLE VARCHAR2(1024) NOT NULL,
    CHAIRS INTEGER NOT NULL,
    ISDONE NUMERIC(1) NOT NULL,
    DESCRIPTION VARCHAR2(1024),
    PRIMARY KEY (ID)
);
CREATE INDEX RESERVATIONS_INX_1 ON RESERVATIONS(DATENEW);

CREATE TABLE RESERVATION_CUSTOMERS (
    ID VARCHAR2(256) NOT NULL,
    CUSTOMER VARCHAR2(256) NOT NULL,
    PRIMARY KEY (ID),
    CONSTRAINT RES_CUST_FK_1 FOREIGN KEY (ID) REFERENCES RESERVATIONS(ID),
    CONSTRAINT RES_CUST_FK_2 FOREIGN KEY (CUSTOMER) REFERENCES CUSTOMERS(ID)
);

CREATE TABLE THIRDPARTIES (
    ID VARCHAR2(256) NOT NULL,
    CIF VARCHAR2(1024) NOT NULL,
    NAME VARCHAR2(1024) NOT NULL,
    ADDRESS VARCHAR2(1024),
    CONTACTCOMM VARCHAR2(1024),
    CONTACTFACT VARCHAR2(1024),
    PAYRULE VARCHAR2(1024),
    FAXNUMBER VARCHAR2(1024),
    PHONENUMBER VARCHAR2(1024),
    MOBILENUMBER VARCHAR2(1024),
    EMAIL VARCHAR2(1024),
    WEBPAGE VARCHAR2(1024),
    NOTES VARCHAR2(1024),
    PRIMARY KEY (ID)
);
CREATE UNIQUE INDEX THIRDPARTIES_CIF_INX ON THIRDPARTIES(CIF);
CREATE UNIQUE INDEX THIRDPARTIES_NAME_INX ON THIRDPARTIES(NAME);

CREATE TABLE SUPPLIERS (
    ID VARCHAR2(256) NOT NULL,
    SEARCHKEY VARCHAR2(1024) NOT NULL,
    TAXID VARCHAR2(1024),
    NAME VARCHAR2(1024) NOT NULL,
    TAXCATEGORY VARCHAR2(256),
    CARD VARCHAR2(1024),
    MAXDEBT DOUBLE PRECISION DEFAULT 0 NOT NULL,
    ADDRESS VARCHAR2(1024),
    ADDRESS2 VARCHAR2(1024),
    POSTAL VARCHAR2(1024),
    CITY VARCHAR2(1024),
    REGION VARCHAR2(1024),
    COUNTRY VARCHAR2(1024),
    FIRSTNAME VARCHAR2(1024),
    LASTNAME VARCHAR2(1024),
    EMAIL VARCHAR2(1024),
    PHONE VARCHAR2(1024),
    PHONE2 VARCHAR2(1024),
    FAX VARCHAR2(1024),
    NOTES VARCHAR2(1024),
    VISIBLE NUMERIC(1) DEFAULT 1 NOT NULL,
    CURDATE TIMESTAMP,
    CURDEBT DOUBLE PRECISION,
    PRIMARY KEY (ID),
    CONSTRAINT SUPPLIERS_TAXCAT FOREIGN KEY (TAXCATEGORY) REFERENCES TAXCUSTCATEGORIES(ID)
);
CREATE UNIQUE INDEX SUPPLIERS_SKEY_INX ON SUPPLIERS(SEARCHKEY);
CREATE INDEX SUPPLIERS_TAXID_INX ON SUPPLIERS(TAXID);
CREATE INDEX SUPPLIERS_NAME_INX ON SUPPLIERS(NAME);
CREATE INDEX SUPPLIERS_CARD_INX ON SUPPLIERS(CARD);

CREATE TABLE SHAREDTICKETS (
    ID VARCHAR2(256) NOT NULL,
    NAME VARCHAR2(1024) NOT NULL,
    CONTENT BLOB,
    USERID VARCHAR2(256) DEFAULT NULL,
    PRIMARY KEY(ID)
);

CREATE TABLE UNITS (
    ID VARCHAR2(256) NOT NULL,
    NAME VARCHAR2(256) NOT NULL,
    SYMBOL VARCHAR2(256) NOT NULL,
    PRIMARY KEY(ID)
);
CREATE UNIQUE INDEX UNITS_NAME_INX ON UNITS(NAME);

CREATE TABLE BANKS (
    ID VARCHAR2(256) NOT NULL,
    NAME VARCHAR2(256) NOT NULL,
    CODE VARCHAR2(256) NOT NULL,
    PRIMARY KEY(ID)
);
CREATE UNIQUE INDEX BANKS_NAME_INX ON BANKS(NAME);

CREATE TABLE MATERIALS_UNITS (
    MATERIAL VARCHAR2(256) NOT NULL,
    UNIT VARCHAR2(256) NOT NULL,
    AMOUNT DOUBLE PRECISION NOT NULL,
    PRICEBUY DOUBLE PRECISION NOT NULL,
    PRIMARY KEY(MATERIAL, UNIT),
    CONSTRAINT MAT_UNIT_FK_1 FOREIGN KEY (MATERIAL) REFERENCES PRODUCTS(ID),
    CONSTRAINT MAT_UNIT_FK_2 FOREIGN KEY (UNIT) REFERENCES UNITS(ID)
);

CREATE TABLE SHIFTS (
  ID VARCHAR2(256) NOT NULL,
  STARTSHIFT TIMESTAMP NOT NULL,
  ENDSHIFT TIMESTAMP,
  PPLID VARCHAR2(256) NOT NULL,
  PRIMARY KEY (ID)
);

CREATE TABLE LEAVES (
  ID VARCHAR2(256) NOT NULL,
  PPLID VARCHAR2(256) NOT NULL,
  NAME VARCHAR2(1024) NOT NULL,
  STARTDATE TIMESTAMP NOT NULL,
  ENDDATE TIMESTAMP NOT NULL,
  NOTES VARCHAR2(1024),
  PRIMARY KEY (ID),
  CONSTRAINT lEAVES_PPLID FOREIGN KEY (PPLID) REFERENCES PEOPLE(ID)
);

CREATE TABLE BREAKS (
  ID VARCHAR2(256) NOT NULL,
  NAME VARCHAR2(1024) NOT NULL,
  NOTES VARCHAR2(1024),
  VISIBLE BOOLEAN NOT NULL,
  PRIMARY KEY (ID)
);

INSERT INTO BREAKS(ID, NAME, VISIBLE, NOTES) VALUES ('0', 'Lunch Break', 1, NULL);
INSERT INTO BREAKS(ID, NAME, VISIBLE, NOTES) VALUES ('1', 'Tea Break', 1, NULL);
INSERT INTO BREAKS(ID, NAME, VISIBLE, NOTES) VALUES ('2', 'Mid Break', 1, NULL);

CREATE TABLE SHIFT_BREAKS (
  ID VARCHAR2(256) NOT NULL,
  SHIFTID VARCHAR2(256) NOT NULL,
  BREAKID VARCHAR2(256) NOT NULL,
  STARTTIME TIMESTAMP NOT NULL,
  ENDTIME TIMESTAMP,
  PRIMARY KEY (ID),
  CONSTRAINT SHIFT_BREAKS_BREAKID FOREIGN KEY (BREAKID) REFERENCES BREAKS(ID),
  CONSTRAINT SHIFT_BREAKS_SHIFTID FOREIGN KEY (SHIFTID) REFERENCES SHIFTS(ID)
);

CREATE TABLE LOGS (
    ID VARCHAR(255) NOT NULL,
    COMPONENT VARCHAR(255),
    USERID VARCHAR(255),
    CONTENT VARCHAR(255),
    TS TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (ID)
);