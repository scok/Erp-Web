import {useState} from 'react';
import {useEffect} from 'react';
import axios from 'axios';

function App() {

    /*수신 받은 데이터*/
    const [receivedData ,setReceivedData] = useState(null);

    /*데이터 로딩시 true로 변경*/
    const [loading ,setLoading] = useState(false);

    /*오류 발생시 정보가 들어 있는 예외 객체*/
    const [error ,setError] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try{
                /*State 초기화*/
                setReceivedData(null);
                setError(null);
                setLoading(true);

                const url = '/userInfo';

                const response = await axios.get(url);

                setReceivedData(response.data);

                console.log('response.data');
                console.log(response.data);


            }catch(err){
                setError(err);

            }/*end try...catch*/

            setLoading(false);

        };/*end fetchData*/

        fetchData();/*called fetchData function*/

        /*command가 변경 되면, 화면을 다시 그려 주도록 합니다*/
    },[]);/*end useEffect*/

    if(loading == true){
        return <div>데이터 로딩 중입니다.</div>;
    }
    if(error == true){
        return <div>오류가 발생했습니다.</div>;
    }
    if(!receivedData){
        return null;
    }

  return (
    <div>
        <a href={`http://localhost:8877/members/mypage/${receivedData.userId}`} class="collapse__sublink akorea">마이페이지</a>
    </div>
  );
}
export default App;

