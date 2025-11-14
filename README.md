Opinion hub

A polling and voting application where everyone can create a poll  or  vote on active polls . 


Features

- Create an account
- Dashboard
- Create a poll 
- Close a poll (only admin)
- Vote on active poll
- See results of closed polls
- See profile


Technologies used

• Java (core language)
• MYSQL (data base)
• Intellij idea(ide)
• JDBC  (data base connectivity)

Requirements to run the project

The following must be installed ;

 Java 8+ 
• MySQL  workbench 
• mysql-connector-j-9.5.0.jar 

Database set up

 run the following  SQL  script on MySQL Workbench before running the code. 

      create database opinion_hub;
       create table users(id int auto_increment primary key, 
                   username varchar(50) , password varchar(50));
                   
       create table polls(id int auto_increment primary key,  creator_id int ,
					topic varchar(300), status varchar(15), ex_date timestamp, 
                    foreign key(creator_id) references users(id) on update cascade on delete cascade);
      create table choices(id int auto_increment primary key,  poll_id int ,
					choice varchar(500),total_votes int,
                    foreign key(poll_id) references polls(id) on update cascade on delete cascade);
                    
           create table voters ( poll_id int ,user_id int,
                    foreign key(poll_id) references polls(id) on update cascade on delete cascade,
                     foreign key(user_id) references users(id) on update cascade on delete cascade
                    );
                        

                  
                   
	•  run the following  SQL  script on MySQL Workbench before running the code. 
<img width="660" height="905" alt="image" src="https://github.com/user-attachments/assets/b803ca78-cff8-4200-b338-3647ff24aeeb" />
