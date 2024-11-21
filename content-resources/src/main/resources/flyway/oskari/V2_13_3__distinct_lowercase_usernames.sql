-- Make sure usernames are case-insensitive.
UPDATE oskari_users set user_name = LOWER(user_name);
CREATE UNIQUE INDEX IF NOT EXISTS oskari_users_roles_unq ON oskari_users_roles (user_id, role_id);
