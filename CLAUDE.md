# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
# Build entire project (requires essentials to be built first due to module dependency)
mvn clean install

# Build specific module
mvn clean install -f essentials/pom.xml
mvn clean install -f chessboard/pom.xml

# Run tests
mvn test

# Run a single test class
mvn test -Dtest=ExceptionsTest

# Generate JaCoCo coverage reports (output in target/jacoco-reports/)
mvn clean install
```

## Project Architecture

This is a multi-module Maven project for chess game logic, written in Java 8.

### Module Structure

**essentials** - Generic Java utilities library
- Configuration management (`com.fgodard.config`)
- Serialization/deserialization framework (`com.fgodard.passivation`)
- File caching (`com.fgodard.files.cache`)
- Learning/spaced repetition system (`com.fgodard.leitner`)
- Bean utilities, string/date helpers, logging, exception hierarchy

**chessboard** - Chess game logic (depends on essentials)
- `com.fgodard.chess.board` - Core board and piece implementations
- `com.fgodard.chess.beans` - Data models (Ply, Position, BoardState)
- `com.fgodard.chess.exception` - Chess-specific exceptions

### Key Classes in chessboard

- **GameBoard**: Central game state manager. Tracks pieces, validates moves, handles castling/en passant, detects check/checkmate
- **Board**: Static utility for cell/coordinate conversions
- **BoardCell**: Enum of all 64 squares with algebraic notation
- **Piece**: Abstract base class; implementations: Bishop, King, Knight, Pawn, Queen, Rook
- **MoveHelper**: Calculates attacking pieces and validates moves
- **PositionExporter**: Imports/exports positions in FEN and LLP (custom) formats

### Design Patterns

- Piece types use strategy pattern for move calculation
- BoardCell enum provides type-safe board coordinates
- GameBoard maintains dual piece lists (white/black) and direct king references for efficient check detection
