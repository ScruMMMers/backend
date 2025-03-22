CREATE TABLE logs (
       id UUID PRIMARY KEY,
       user_id UUID NOT NULL,
       message TEXT NOT NULL,
       type VARCHAR(50) NOT NULL,
       created_at TIMESTAMPTZ NOT NULL,
       edited_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE tags (
       id UUID PRIMARY KEY,
       company_id UUID NOT NULL,
       name TEXT NOT NULL
);

CREATE TABLE reactions (
         id UUID PRIMARY KEY,
         emoji TEXT NOT NULL
);

CREATE TABLE log_reactions (
       id UUID PRIMARY KEY,
       log_id UUID NOT NULL,
       reaction_id UUID NOT NULL,
       user_id UUID NOT NULL,
       CONSTRAINT fk_log_reactions_log FOREIGN KEY (log_id)
           REFERENCES logs (id) ON DELETE CASCADE,
       CONSTRAINT fk_log_reactions_reaction FOREIGN KEY (reaction_id)
           REFERENCES reactions (id) ON DELETE CASCADE
);

CREATE TABLE log_tags (
     log_id UUID NOT NULL,
     tag_id UUID NOT NULL,
     PRIMARY KEY (log_id, tag_id),
     CONSTRAINT fk_log_tags_log FOREIGN KEY (log_id)
         REFERENCES logs (id) ON DELETE CASCADE,
     CONSTRAINT fk_log_tags_tag FOREIGN KEY (tag_id)
         REFERENCES tags (id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS category;

INSERT INTO reactions (id, emoji) VALUES
       ('d3108367-3b0b-4ee6-8264-05e890f4fc74', '👍'),
       ('4ca0f4eb-f4b5-4739-8c58-c649a33964da', '👎'),
       ('3a221f98-7f0a-472d-8a27-568e489316ef', '❤️'),
       ('2bfc0ac7-100b-45e7-b04a-5d4a9047a879', '😂');