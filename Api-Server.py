from flask import Flask
from flask_restful import Api, Resource, reqparse

import sqlite3

app = Flask(__name__)
api = Api(app)


# fetch 결과를 Dict<컬럼명, 값> 형태로 변환
def dict_factory(cursor, row):
    d = {}
    for idx, col in enumerate(cursor.description):
        d[col[0]] = row[idx]
    return d


# 게시판 요청 처리 클래스
class Board:

    # DB 초기화 클래스
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

    # 게시판 조회 요청 처리 클래스
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

    # 게시판 입력 요청 처리 클래스
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


# 핸들러 별 경로 지정
api.add_resource(Board.Insert, "/board/insert")
api.add_resource(Board.Lookup, "/board/lookup")


if __name__ == "__main__":
    Board.init_table()
    app.run(debug=True)
