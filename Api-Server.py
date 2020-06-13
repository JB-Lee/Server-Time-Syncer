from flask import Flask
from flask_restful import Api, Resource, reqparse

import sqlite3

app = Flask(__name__)
api = Api(app)


def dict_factory(cursor, row):
    d = {}
    for idx, col in enumerate(cursor.description):
        d[col[0]] = row[idx]
    return d


class Board:

    @staticmethod
    def init_table():
        with sqlite3.connect("project.db") as conn:
            init_sql = "CREATE TABLE IF NOT EXISTS board(" \
                      "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," \
                      "writer TEXT NOT NULL," \
                      "category TEXT NOT NULL," \
                      "content TEXT NOT NULL," \
                      "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL)"
            conn.execute(init_sql)
            conn.commit()

    class Lookup(Resource):
        @staticmethod
        def get():
            try:
                parser = reqparse.RequestParser()
                parser.add_argument("cat", type=str)
                parser.add_argument("nums", type=int)
                args = parser.parse_args()

                with sqlite3.connect("project.db") as conn:
                    conn.row_factory = dict_factory
                    cur = conn.cursor()

                    sql = "SELECT * FROM board WHERE category=? ORDER BY timestamp DESC LIMIT ?"
                    cur.execute(sql, (args['cat'], args['nums']))
                    results = cur.fetchall()

                return {"status": "Success", "content": results}
            except Exception as e:
                return {"status": "Fail", "content": str(e)}

    class Insert(Resource):
        @staticmethod
        def get():
            try:
                parser = reqparse.RequestParser()
                parser.add_argument("writer", type=str)
                parser.add_argument("cat", type=str)
                parser.add_argument("content", type=str)
                args = parser.parse_args()

                with sqlite3.connect("project.db") as conn:
                    conn.row_factory = dict_factory
                    cur = conn.cursor()

                    sql = "INSERT INTO board(writer, category, content) VALUES(?, ?, ?)"
                    cur.execute(sql, (args['writer'], args['cat'], args['content']))

                return {"status": "Success", "content": None}
            except Exception as e:
                return {"status": "Fail", "content": str(e)}


api.add_resource(Board.Insert, "/board/insert")
api.add_resource(Board.Lookup, "/board/lookup")


if __name__ == "__main__":
    Board.init_table()
    app.run(debug=True)
