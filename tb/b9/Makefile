


srvc: srvc_stp
	cd b3.w3.jt1; mvn jetty:run &
	cd b3.w3.rs1; mvn jetty:run &
	cd b3.w3.rs2; mvn jetty:run &
	cd b3.w3.sg1; mvn jetty:run &
	cd b3.w3.sg2; mvn jetty:run &

srvc_stp:
	cd b3.w3.jt1; mvn jetty:stop &
	cd b3.w3.rs1; mvn jetty:stop &
	cd b3.w3.rs2; mvn jetty:stop &
	cd b3.w3.sg1; mvn jetty:stop &
	cd b3.w3.sg2; mvn jetty:stop &

srvc_pack:
	cd b3.w3.sg1; make mvn_pack; make jt_set

srvc_cln:
	cd b3.w3.jt1; mvn eclipse:clean &
	cd b3.w3.rs1; mvn eclipse:clean &
	cd b3.w3.rs2; mvn eclipse:clean &
	cd b3.w3.sg1; mvn eclipse:clean &
	cd b3.w3.sg2; mvn eclipse:clean &

port_chk:
	lsof -i :8001
	netstat -nlp | grep 8001
