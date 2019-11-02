Drop table if exists oauth_client_details;
create table oauth_client_details (
    client_id VARCHAR(255) PRIMARY KEY,
    resource_ids VARCHAR(255),
    client_secret VARCHAR(255),
    scope VARCHAR(255),
    authorized_grant_types VARCHAR(255),
    web_server_redirect_uri VARCHAR(255),
    authorities VARCHAR(255),
    access_token_validity INTEGER,
    refresh_token_validity INTEGER,
    additional_information TEXT,
    autoapprove VARCHAR (255) default 'false'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

Drop table if exists oauth_access_token;
create table oauth_access_token (
    token_id VARCHAR(255),
    token BLOB,
    authentication_id VARCHAR(255),
    user_name VARCHAR(255),
    client_id VARCHAR(255),
    authentication BLOB,
    refresh_token VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

Drop table if exists oauth_refresh_token;
create table oauth_refresh_token (
    token_id VARCHAR(255),
    token BLOB,
    authentication BLOB
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

Drop table if exists oauth_code;
create table oauth_code (
    code VARCHAR(255),
    authentication BLOB
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Add indexes
create index token_id_index on oauth_access_token (token_id);
create index authentication_id_index on oauth_access_token (authentication_id);
create index user_name_index on oauth_access_token (user_name);
create index client_id_index on oauth_access_token (client_id);
create index refresh_token_index on oauth_access_token (refresh_token);
create index token_id_index on oauth_refresh_token (token_id);
create index code_index on oauth_code (code);

-- INSERT DEFAULT DATA
INSERT INTO oauth_client_details VALUES ('dev', '', 'dev', 'app', 'authorization_code', 'http://localhost:7777/', '', '3600', '3600', '{"country":"CN","country_code":"086"}', 'TAIJI');

INSERT INTO oauth_client_details
(client_id, client_secret, scope, authorized_grant_types,
 web_server_redirect_uri, authorities, access_token_validity,
 refresh_token_validity, additional_information, autoapprove)
VALUES
('fooClientIdPassword', 'secret', 'foo,read,write',
 'password,authorization_code,refresh_token,client_credentials', null, null, 36000, 36000, null, true);
INSERT INTO oauth_client_details
(client_id, client_secret, scope, authorized_grant_types,
 web_server_redirect_uri, authorities, access_token_validity,
 refresh_token_validity, additional_information, autoapprove)
VALUES
('sampleClientId', 'secret', 'read,write,foo,bar',
 'implicit', null, null, 36000, 36000, null, false);
INSERT INTO oauth_client_details
(client_id, client_secret, scope, authorized_grant_types,
 web_server_redirect_uri, authorities, access_token_validity,
 refresh_token_validity, additional_information, autoapprove)
VALUES
('barClientIdPassword', 'secret', 'bar,read,write',
 'password,authorization_code,refresh_token', null, null, 36000, 36000, null, true);