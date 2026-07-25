CREATE TABLE IF NOT EXISTS schema_version_marker (
    id BIGSERIAL PRIMARY KEY,
    marker VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

INSERT INTO schema_version_marker (marker)
VALUES ('backend_baseline')
ON CONFLICT (marker) DO NOTHING;
