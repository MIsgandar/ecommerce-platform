CREATE TABLE categories (

    id UUID PRIMARY KEY,

    name VARCHAR(100) NOT NULL,

    description VARCHAR(500),

    CONSTRAINT uk_category_name
        UNIQUE (name)

);


CREATE TABLE products (

    id UUID PRIMARY KEY,

    sku VARCHAR(100) NOT NULL,

    name VARCHAR(255) NOT NULL,

    description VARCHAR(2000),

    price NUMERIC(19,4) NOT NULL,

    quantity INTEGER NOT NULL,

    status VARCHAR(30) NOT NULL,

    category_id UUID NOT NULL,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL,

    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT uk_product_sku
        UNIQUE (sku),

    CONSTRAINT fk_product_category
        FOREIGN KEY (category_id)
        REFERENCES categories(id)

);


CREATE INDEX idx_products_category_id
    ON products(category_id);


CREATE INDEX idx_products_status
    ON products(status);