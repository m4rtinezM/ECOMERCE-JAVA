CREATE DATABASE IF NOT EXISTS Videojuegos;
USE Videojuegos;
DESCRIBE productos;
DROP TABLE IF EXISTS productos;

CREATE TABLE Productos(
    id_producto VARCHAR(50) NOT NULL PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL,
    genero VARCHAR(50) NOT NULL,
    precio DECIMAL(10,2) NOT NULL
);

INSERT INTO Productos (id_producto, nombre, genero, precio) VALUES
('GAME001', 'The Witcher 3: Wild Hunt', 'RPG', 39.99),
('GAME002', 'Cyberpunk 2077', 'RPG', 49.99),
('GAME003', 'Grand Theft Auto V', 'Acción', 29.99),
('GAME004', 'Red Dead Redemption 2', 'Aventura', 59.99),
('GAME005', 'Counter-Strike 2', 'FPS', 0.00),
('GAME006', 'Dota 2', 'MOBA', 0.00),
('GAME007', 'Elden Ring', 'RPG', 59.99),
('GAME008', 'Baldur''s Gate 3', 'RPG', 59.99),
('GAME009', 'Call of Duty: Warzone', 'Battle Royale', 0.00),
('GAME010', 'Minecraft', 'Sandbox', 26.95),
('GAME011', 'Fortnite', 'Battle Royale', 0.00),
('GAME012', 'Apex Legends', 'Battle Royale', 0.00),
('GAME013', 'Valorant', 'FPS', 0.00),
('GAME014', 'League of Legends', 'MOBA', 0.00),
('GAME015', 'Overwatch 2', 'FPS', 0.00),
('GAME016', 'World of Warcraft', 'MMORPG', 14.99),
('GAME017', 'Diablo IV', 'RPG', 69.99),
('GAME018', 'StarCraft II', 'Estrategia', 0.00),
('GAME019', 'The Elder Scrolls V: Skyrim', 'RPG', 39.99),
('GAME020', 'Fallout 4', 'RPG', 29.99),
('GAME021', 'Doom Eternal', 'FPS', 39.99),
('GAME022', 'Tomb Raider', 'Aventura', 19.99),
('GAME023', 'Resident Evil 4 Remake', 'Terror', 59.99),
('GAME024', 'Street Fighter 6', 'Lucha', 59.99),
('GAME025', 'Assassin''s Creed Valhalla', 'RPG', 59.99),
('GAME026', 'Far Cry 6', 'FPS', 59.99),
('GAME027', 'Rainbow Six Siege', 'FPS', 19.99),
('GAME028', 'Hollow Knight', 'Metroidvania', 14.99),
('GAME029', 'Celeste', 'Plataformas', 19.99),
('GAME030', 'Stardew Valley', 'Simulación', 14.99),
('GAME031', 'Terraria', 'Sandbox', 9.99),
('GAME032', 'Rocket League', 'Deportes', 0.00),
('GAME033', 'Dead by Daylight', 'Terror', 19.99),
('GAME034', 'Phasmophobia', 'Terror', 13.99),
('GAME035', 'Sea of Thieves', 'Aventura', 39.99),
('GAME036', 'Forza Horizon 5', 'Carreras', 59.99),
('GAME037', 'Microsoft Flight Simulator', 'Simulación', 59.99),
('GAME038', 'Age of Empires IV', 'Estrategia', 59.99),
('GAME039', 'Civilization VI', 'Estrategia', 59.99),
('GAME040', 'Cities: Skylines', 'Simulación', 29.99);

SELECT * FROM Productos;