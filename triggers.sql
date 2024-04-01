CREATE FUNCTION set_placement() 
RETURNS trigger AS $$
BEGIN
    if(new.position = 1) then
        new.medal = 'Gold';
    elsif(new.position = 2) then
        new.medal = 'Silver';
    elsif(new.position = 3) then
        new.medal = 'Bronze';
    elseif(new.position is null) then  
        new.medal = NULL;
    end if;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER ASSIGN_MEDAL
    BEFORE INSERT OR UPDATE 
    ON olympic_schema.placement
    FOR EACH ROW
EXECUTE FUNCTION set_placement();

