PGDMP     ,                	    z         
   atividades    14.5    14.5     �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            �           1262    16457 
   atividades    DATABASE     l   CREATE DATABASE "atividades" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'Portuguese_Brazil.1252';
    DROP DATABASE "atividades";
                postgres    false                        2615    16458    tasks    SCHEMA        CREATE SCHEMA "tasks";
    DROP SCHEMA "tasks";
                postgres    false            �            1259    16493    tarefas    TABLE     �   CREATE TABLE "tasks"."tarefas" (
    "id" bigint NOT NULL,
    "concluido" boolean,
    "descricao" character varying(255),
    "id_usuario" bigint
);
    DROP TABLE "tasks"."tarefas";
       tasks         heap    postgres    false    5            �            1259    16492    tarefas_id_seq    SEQUENCE     z   CREATE SEQUENCE "tasks"."tarefas_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 (   DROP SEQUENCE "tasks"."tarefas_id_seq";
       tasks          postgres    false    5    211            �           0    0    tarefas_id_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE "tasks"."tarefas_id_seq" OWNED BY "tasks"."tarefas"."id";
          tasks          postgres    false    210            �            1259    16500    usuario    TABLE     �   CREATE TABLE "tasks"."usuario" (
    "id" bigint NOT NULL,
    "email" character varying(255),
    "nome" character varying(255),
    "senha" character varying(255)
);
    DROP TABLE "tasks"."usuario";
       tasks         heap    postgres    false    5            �            1259    16499    usuario_id_seq    SEQUENCE     z   CREATE SEQUENCE "tasks"."usuario_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 (   DROP SEQUENCE "tasks"."usuario_id_seq";
       tasks          postgres    false    213    5            �           0    0    usuario_id_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE "tasks"."usuario_id_seq" OWNED BY "tasks"."usuario"."id";
          tasks          postgres    false    212            b           2604    16496 
   tarefas id    DEFAULT     t   ALTER TABLE ONLY "tasks"."tarefas" ALTER COLUMN "id" SET DEFAULT "nextval"('"tasks"."tarefas_id_seq"'::"regclass");
 >   ALTER TABLE "tasks"."tarefas" ALTER COLUMN "id" DROP DEFAULT;
       tasks          postgres    false    210    211    211            c           2604    16503 
   usuario id    DEFAULT     t   ALTER TABLE ONLY "tasks"."usuario" ALTER COLUMN "id" SET DEFAULT "nextval"('"tasks"."usuario_id_seq"'::"regclass");
 >   ALTER TABLE "tasks"."usuario" ALTER COLUMN "id" DROP DEFAULT;
       tasks          postgres    false    213    212    213            �          0    16493    tarefas 
   TABLE DATA                 tasks          postgres    false    211   �       �          0    16500    usuario 
   TABLE DATA                 tasks          postgres    false    213   �                   0    0    tarefas_id_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('"tasks"."tarefas_id_seq"', 61, true);
          tasks          postgres    false    210                       0    0    usuario_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('"tasks"."usuario_id_seq"', 6, true);
          tasks          postgres    false    212            e           2606    16498    tarefas tarefas_pkey 
   CONSTRAINT     Y   ALTER TABLE ONLY "tasks"."tarefas"
    ADD CONSTRAINT "tarefas_pkey" PRIMARY KEY ("id");
 C   ALTER TABLE ONLY "tasks"."tarefas" DROP CONSTRAINT "tarefas_pkey";
       tasks            postgres    false    211            g           2606    16507    usuario usuario_pkey 
   CONSTRAINT     Y   ALTER TABLE ONLY "tasks"."usuario"
    ADD CONSTRAINT "usuario_pkey" PRIMARY KEY ("id");
 C   ALTER TABLE ONLY "tasks"."usuario" DROP CONSTRAINT "usuario_pkey";
       tasks            postgres    false    213            h           2606    16508    tarefas fk_usuario    FK CONSTRAINT     �   ALTER TABLE ONLY "tasks"."tarefas"
    ADD CONSTRAINT "fk_usuario" FOREIGN KEY ("id_usuario") REFERENCES "tasks"."usuario"("id");
 A   ALTER TABLE ONLY "tasks"."tarefas" DROP CONSTRAINT "fk_usuario";
       tasks          postgres    false    213    3175    211            �   
   x���          �   
   x���         