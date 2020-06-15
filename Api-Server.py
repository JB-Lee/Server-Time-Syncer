import hashlib
import random
import sqlite3
import string

from flask import Flask
from flask_restful import Api, Resource, reqparse

app = Flask(__name__)
api = Api(app)


def dict_factory(cursor, row):
    d = {}
    for idx, col in enumerate(cursor.description):
        d[col[0]] = row[idx]
    return d


class Hello(Resource):
    @staticmethod
    def get():
        return {"status": "Success", "content": {"msg": "ServerTimeSyncer"}}


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

                    if args['cat']:
                        sql = "SELECT * FROM board WHERE category=? ORDER BY timestamp DESC LIMIT ?"
                        cur.execute(sql, (args['cat'], args['nums']))
                    else:
                        sql = "SELECT * FROM board ORDER BY timestamp DESC LIMIT ?"
                        cur.execute(sql, (args['nums'],))

                    results = cur.fetchall()

                return {"status": "Success", "content": results}
            except Exception as e:
                return {"status": "Fail", "content": {"msg": str(e)}}

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

                return {"status": "Success", "content": {"None": None}}
            except Exception as e:
                return {"status": "Fail", "content": {"msg": str(e)}}


class User:
    @staticmethod
    def init_table():
        with sqlite3.connect("project.db") as conn:
            init_sql = "CREATE TABLE IF NOT EXISTS user(" \
                       "id TEXT PRIMARY KEY NOT NULL," \
                       "email TEXT NOT NULL," \
                       "pw TEXT NOT NULL," \
                       "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL)"
            conn.execute(init_sql)

    @staticmethod
    def is_exist_user(user):
        with sqlite3.connect("project.db") as conn:
            cur = conn.cursor()

            sql = "SELECT COUNT(*) FROM user WHERE id=?"
            cur.execute(sql, (user,))

            result = cur.fetchone()
        return result[0] != 0

    @staticmethod
    def get_pw_hash(pw):
        return hashlib.sha256(pw.encode()).hexdigest()

    @staticmethod
    def gen_password(char_length=4, digit_length=4):
        return ''.join(random.choice(string.ascii_lowercase) for _ in range(char_length)) + \
               ''.join(random.choice(string.digits) for _ in range(digit_length))

    class Signup(Resource):
        @staticmethod
        def get():
            try:
                parser = reqparse.RequestParser()
                parser.add_argument("id", type=str)
                parser.add_argument("email", type=str)
                parser.add_argument("pw", type=str)
                args = parser.parse_args()

                if User.is_exist_user(args['id']):
                    return {"status": "Fail", "content": {"reason": "User Exist"}}

                with sqlite3.connect("project.db") as conn:
                    conn.row_factory = dict_factory
                    cur = conn.cursor()

                    sql = "INSERT INTO user(id, email, pw) VALUES(?, ?, ?)"

                    cur.execute(sql, (args['id'], args['email'], User.get_pw_hash(args['pw'])))

                return {"status": "Success", "content": {"None": None}}

            except Exception as e:
                return {"status": "Fail", "content": {"msg": str(e)}}

    class Login(Resource):
        @staticmethod
        def get():
            try:
                parser = reqparse.RequestParser()
                parser.add_argument("id", type=str)
                parser.add_argument("pw", type=str)
                args = parser.parse_args()

                with sqlite3.connect("project.db") as conn:
                    conn.row_factory = dict_factory
                    cur = conn.cursor()

                    sql = "SELECT id, email FROM user WHERE id=? AND pw=?"
                    cur.execute(sql, (args['id'], User.get_pw_hash(args['pw'])))

                    result = cur.fetchone()

                if result:
                    return {"status": "Success", "content": result}
                else:
                    return {"status": "Fail", "content": {"reason": "Login Fail"}}

            except Exception as e:
                return {"status": "Fail", "content": {"msg": str(e)}}

    class FindPassword(Resource):
        @staticmethod
        def get():
            try:
                parser = reqparse.RequestParser()
                parser.add_argument("id", type=str)
                parser.add_argument("email", type=str)
                args = parser.parse_args()

                with sqlite3.connect("project.db") as conn:
                    cur = conn.cursor()

                    sql = "SELECT COUNT(*) FROM user WHERE id=? AND email=?"
                    cur.execute(sql, (args['id'], args['email']))

                    result = cur.fetchone()

                if result[0] == 0:
                    return {"status": "Fail", "content": {"reason": "Invalid Account"}}

                gen_pw = User.gen_password()

                with sqlite3.connect("project.db") as conn:
                    cur = conn.cursor()

                    sql = "UPDATE user SET pw=? WHERE id=?"
                    cur.execute(sql, (User.get_pw_hash(gen_pw), args['id']))

                return {"status": "Success", "content": {"changed": gen_pw}}

            except Exception as e:
                return {"status": "Fail", "content": {"msg": str(e)}}

    class ChangePassword(Resource):
        @staticmethod
        def get():
            try:
                parser = reqparse.RequestParser()
                parser.add_argument("id", type=str)
                parser.add_argument("pw", type=str)
                args = parser.parse_args()

                with sqlite3.connect("project.db") as conn:
                    cur = conn.cursor()

                    sql = "SELECT COUNT(*) FROM user WHERE id=? AND pw=?"
                    cur.execute(sql, (args['id'], User.get_pw_hash(args['pw'])))

                    result = cur.fetchone()

                if result == 0:
                    return {"status": "Fail", "content": {"reason": "Invalid Account"}}

                with sqlite3.connect("project.db") as conn:
                    cur = conn.cursor()

                    sql = "UPDATE user SET pw=? WHERE id=?"
                    cur.execute(sql, (User.get_pw_hash(args['pw']), args['id']))

                return {"status": "Success", "content": {"None": None}}

            except Exception as e:
                return {"status": "Fail", "content": {"msg": str(e)}}

    class Check(Resource):
        @staticmethod
        def get():
            try:
                parser = reqparse.RequestParser()
                parser.add_argument("id", type=str)
                args = parser.parse_args()

                return {"status": "Success", "content": {"result": User.is_exist_user(args['id'])}}

            except Exception as e:
                return {"status": "Fail", "content": {"msg": str(e)}}


api.add_resource(Hello, "/hello")

api.add_resource(Board.Insert, "/board/insert")
api.add_resource(Board.Lookup, "/board/lookup")

api.add_resource(User.Signup, "/user/signup")
api.add_resource(User.Login, "/user/login")
api.add_resource(User.Check, "/user/check")

api.add_resource(User.ChangePassword, "/user/password/change")
api.add_resource(User.FindPassword, "/user/password/find")

if __name__ == "__main__":
    Board.init_table()
    User.init_table()
    app.run(host='0.0.0.0', debug=True)
