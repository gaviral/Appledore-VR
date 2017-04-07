-- bit of code puts the script in a module we can be imported by another script with a "require" statement
local moduleName = "firebasePost" 
local M = {}
_G[moduleName] = M


function M.download(host, port, url, callback)
    -- open a file on the WiFi dongle flash file system. File is created for writing
    --file.remove(path);
    --file.open(path, "w+")
	
	uart.setup(0, 115200, 8, uart.PARITY_NONE, uart.STOPBITS_1, 0)
	
    -- create a connection to the web site here
    conn=net.createConnection(net.TCP, false) 
    payloadFound = false

    -- this call back function is called when a packet of data arrives from the web site            
    conn:on("receive", function(conn, payload)
	-- optional next line shows the packets of data arriving from the web site
        --print(string.len(payload))
        
        if (payloadFound == true) then
            --file.write(payload)
            --file.flush()
			uart.write(0, payload)
        else
	    -- look for the \r\n\r\n that separates an http response header from the body
            if (string.find(payload,"\r\n\r\n") ~= nil) then
                uart.write(0, string.sub(payload,string.find(payload,"\r\n\r\n") + 4))
                --file.flush()
                payloadFound = true
            end
        end

        payload = nil
        collectgarbage()
    end)

    -- this call back function is called when we disconnect from the web site
    conn:on("disconnection", function(conn) 
        conn = nil
        --file.close()
		--print("d");
		print("DONEdone")
        callback("ok")
    end)
    
    -- this call back function is called when a connection to the web site is made
    conn:on("connection", function(conn)
        conn:send("POST /"..url.." HTTP/1.0\r\n"..
              "Host: "..host.."\r\n"..
              "Connection: close\r\n"..
              "Accept-Charset: utf-8\r\n"..
              "Accept-Encoding: \r\n"..
              "User-Agent: Mozilla/4.0 (compatible; esp8266 Lua; Windows NT 5.1)\r\n".. 
              "Accept: */*\r\n\r\n")
    end)

    -- connect to the web site here
    conn:connect(port,host)
end
-- the following return statement returns the script and all it's functions and variables to the caller, i.e. exports them
return M