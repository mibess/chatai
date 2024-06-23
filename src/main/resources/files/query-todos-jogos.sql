SELECT 
    tc.nome || ' ' || 
    CASE 
        WHEN j.gols_time_casa IS NULL THEN '-' 
        ELSE j.gols_time_casa::text 
    END || ' x ' || 
    tv.nome || ' ' || 
    CASE 
        WHEN j.gols_time_visitante IS NULL THEN '-' 
        ELSE j.gols_time_visitante::text 
    END AS resultado_jogo
FROM jogo j
INNER JOIN time tc ON tc.id = j.time_casa_id
INNER JOIN time tv ON tv.id = j.time_visitante_id;

