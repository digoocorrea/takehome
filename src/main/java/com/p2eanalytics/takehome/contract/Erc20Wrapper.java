package com.p2eanalytics.takehome.contract;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.3.0.
 */
public class Erc20Wrapper extends Contract {
    private static final String BINARY = "60806040523480156200001157600080fd5b506040518060400160405280601281526020017f547265617375726520556e6465722053656100000000000000000000000000008152506040518060400160405280600381526020017f545553000000000000000000000000000000000000000000000000000000000081525081600390805190602001906200009692919062000219565b508060049080519060200190620000af92919062000219565b505050620000d2620000c66200014b60201b60201c565b6200015360201b60201c565b6000600560146101000a81548160ff0219169083151502179055506001600660003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055506200032e565b600033905090565b6000600560009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905081600560006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508173ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a35050565b8280546200022790620002c9565b90600052602060002090601f0160209004810192826200024b576000855562000297565b82601f106200026657805160ff191683800117855562000297565b8280016001018555821562000297579182015b828111156200029657825182559160200191906001019062000279565b5b509050620002a69190620002aa565b5090565b5b80821115620002c5576000816000905550600101620002ab565b5090565b60006002820490506001821680620002e257607f821691505b60208210811415620002f957620002f8620002ff565b5b50919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052602260045260246000fd5b612433806200033e6000396000f3fe608060405234801561001057600080fd5b50600436106101375760003560e01c806370a08231116100b8578063a457c2d71161007c578063a457c2d714610306578063a9059cbb14610336578063cf456ae714610366578063dd62ed3e14610382578063f2fde38b146103b2578063f46eccc4146103ce57610137565b806370a0823114610286578063715018a6146102b65780638456cb59146102c05780638da5cb5b146102ca57806395d89b41146102e857610137565b806339509351116100ff57806339509351146101f65780633f4ba83a1461022657806340c10f191461023057806342966c681461024c5780635c975abb1461026857610137565b806306fdde031461013c578063095ea7b31461015a57806318160ddd1461018a57806323b872dd146101a8578063313ce567146101d8575b600080fd5b6101446103fe565b6040516101519190611bce565b60405180910390f35b610174600480360381019061016f91906118b1565b610490565b6040516101819190611bb3565b60405180910390f35b6101926104ae565b60405161019f9190611dd0565b60405180910390f35b6101c260048036038101906101bd9190611826565b6104b8565b6040516101cf9190611bb3565b60405180910390f35b6101e06105b0565b6040516101ed9190611deb565b60405180910390f35b610210600480360381019061020b91906118b1565b6105b9565b60405161021d9190611bb3565b60405180910390f35b61022e610665565b005b61024a600480360381019061024591906118b1565b6106eb565b005b610266600480360381019061026191906118ed565b610787565b005b610270610794565b60405161027d9190611bb3565b60405180910390f35b6102a0600480360381019061029b91906117c1565b6107ab565b6040516102ad9190611dd0565b60405180910390f35b6102be6107f3565b005b6102c861087b565b005b6102d2610901565b6040516102df9190611b98565b60405180910390f35b6102f061092b565b6040516102fd9190611bce565b60405180910390f35b610320600480360381019061031b91906118b1565b6109bd565b60405161032d9190611bb3565b60405180910390f35b610350600480360381019061034b91906118b1565b610aa8565b60405161035d9190611bb3565b60405180910390f35b610380600480360381019061037b9190611875565b610ac6565b005b61039c600480360381019061039791906117ea565b610beb565b6040516103a99190611dd0565b60405180910390f35b6103cc60048036038101906103c791906117c1565b610c72565b005b6103e860048036038101906103e391906117c1565b610d6a565b6040516103f59190611bb3565b60405180910390f35b60606003805461040d90611f34565b80601f016020809104026020016040519081016040528092919081815260200182805461043990611f34565b80156104865780601f1061045b57610100808354040283529160200191610486565b820191906000526020600020905b81548152906001019060200180831161046957829003601f168201915b5050505050905090565b60006104a461049d610d8a565b8484610d92565b6001905092915050565b6000600254905090565b60006104c5848484610f5d565b6000600160008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000610510610d8a565b73ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054905082811015610590576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161058790611cf0565b60405180910390fd5b6105a48561059c610d8a565b858403610d92565b60019150509392505050565b60006012905090565b600061065b6105c6610d8a565b8484600160006105d4610d8a565b73ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008873ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020546106569190611e22565b610d92565b6001905092915050565b61066d610d8a565b73ffffffffffffffffffffffffffffffffffffffff1661068b610901565b73ffffffffffffffffffffffffffffffffffffffff16146106e1576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016106d890611d10565b60405180910390fd5b6106e96111de565b565b33600660008273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff16610778576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161076f90611cb0565b60405180910390fd5b6107828383611280565b505050565b61079133826113e0565b50565b6000600560149054906101000a900460ff16905090565b60008060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020549050919050565b6107fb610d8a565b73ffffffffffffffffffffffffffffffffffffffff16610819610901565b73ffffffffffffffffffffffffffffffffffffffff161461086f576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161086690611d10565b60405180910390fd5b61087960006115b7565b565b610883610d8a565b73ffffffffffffffffffffffffffffffffffffffff166108a1610901565b73ffffffffffffffffffffffffffffffffffffffff16146108f7576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016108ee90611d10565b60405180910390fd5b6108ff61167d565b565b6000600560009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b60606004805461093a90611f34565b80601f016020809104026020016040519081016040528092919081815260200182805461096690611f34565b80156109b35780601f10610988576101008083540402835291602001916109b3565b820191906000526020600020905b81548152906001019060200180831161099657829003601f168201915b5050505050905090565b600080600160006109cc610d8a565b73ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054905082811015610a89576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610a8090611d90565b60405180910390fd5b610a9d610a94610d8a565b85858403610d92565b600191505092915050565b6000610abc610ab5610d8a565b8484610f5d565b6001905092915050565b610ace610d8a565b73ffffffffffffffffffffffffffffffffffffffff16610aec610901565b73ffffffffffffffffffffffffffffffffffffffff1614610b42576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610b3990611d10565b60405180910390fd5b80600660008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055508173ffffffffffffffffffffffffffffffffffffffff167f1f96bc657d385fd83da973a43f2ad969e6d96b6779b779571a7306db7ca1cd0082604051610bdf9190611bb3565b60405180910390a25050565b6000600160008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054905092915050565b610c7a610d8a565b73ffffffffffffffffffffffffffffffffffffffff16610c98610901565b73ffffffffffffffffffffffffffffffffffffffff1614610cee576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610ce590611d10565b60405180910390fd5b600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff161415610d5e576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610d5590611c50565b60405180910390fd5b610d67816115b7565b50565b60066020528060005260406000206000915054906101000a900460ff1681565b600033905090565b600073ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff161415610e02576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610df990611d70565b60405180910390fd5b600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff161415610e72576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610e6990611c70565b60405180910390fd5b80600160008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055508173ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff167f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b92583604051610f509190611dd0565b60405180910390a3505050565b600073ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff161415610fcd576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610fc490611d50565b60405180910390fd5b600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff16141561103d576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161103490611bf0565b60405180910390fd5b611048838383611720565b60008060008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020549050818110156110ce576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016110c590611c90565b60405180910390fd5b8181036000808673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081905550816000808573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008282546111619190611e22565b925050819055508273ffffffffffffffffffffffffffffffffffffffff168473ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef846040516111c59190611dd0565b60405180910390a36111d8848484611778565b50505050565b6111e6610794565b611225576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161121c90611c10565b60405180910390fd5b6000600560146101000a81548160ff0219169083151502179055507f5db9ee0a495bf2e6ff9c91a7834c1ba4fdd244a5e8aa4e537bd38aeae4b073aa611269610d8a565b6040516112769190611b98565b60405180910390a1565b600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff1614156112f0576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016112e790611db0565b60405180910390fd5b6112fc60008383611720565b806002600082825461130e9190611e22565b92505081905550806000808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008282546113639190611e22565b925050819055508173ffffffffffffffffffffffffffffffffffffffff16600073ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef836040516113c89190611dd0565b60405180910390a36113dc60008383611778565b5050565b600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff161415611450576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161144790611d30565b60405180910390fd5b61145c82600083611720565b60008060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020549050818110156114e2576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016114d990611c30565b60405180910390fd5b8181036000808573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000208190555081600260008282546115399190611e78565b92505081905550600073ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef8460405161159e9190611dd0565b60405180910390a36115b283600084611778565b505050565b6000600560009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905081600560006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508173ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a35050565b611685610794565b156116c5576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016116bc90611cd0565b60405180910390fd5b6001600560146101000a81548160ff0219169083151502179055507f62e78cea01bee320cd4e420270b5ea74000d11b0c9f74754ebdbfc544b05a258611709610d8a565b6040516117169190611b98565b60405180910390a1565b611728610794565b15611768576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161175f90611cd0565b60405180910390fd5b61177383838361177d565b505050565b505050565b505050565b600081359050611791816123b8565b92915050565b6000813590506117a6816123cf565b92915050565b6000813590506117bb816123e6565b92915050565b6000602082840312156117d357600080fd5b60006117e184828501611782565b91505092915050565b600080604083850312156117fd57600080fd5b600061180b85828601611782565b925050602061181c85828601611782565b9150509250929050565b60008060006060848603121561183b57600080fd5b600061184986828701611782565b935050602061185a86828701611782565b925050604061186b868287016117ac565b9150509250925092565b6000806040838503121561188857600080fd5b600061189685828601611782565b92505060206118a785828601611797565b9150509250929050565b600080604083850312156118c457600080fd5b60006118d285828601611782565b92505060206118e3858286016117ac565b9150509250929050565b6000602082840312156118ff57600080fd5b600061190d848285016117ac565b91505092915050565b61191f81611eac565b82525050565b61192e81611ebe565b82525050565b600061193f82611e06565b6119498185611e11565b9350611959818560208601611f01565b61196281611fc4565b840191505092915050565b600061197a602383611e11565b915061198582611fd5565b604082019050919050565b600061199d601483611e11565b91506119a882612024565b602082019050919050565b60006119c0602283611e11565b91506119cb8261204d565b604082019050919050565b60006119e3602683611e11565b91506119ee8261209c565b604082019050919050565b6000611a06602283611e11565b9150611a11826120eb565b604082019050919050565b6000611a29602683611e11565b9150611a348261213a565b604082019050919050565b6000611a4c601083611e11565b9150611a5782612189565b602082019050919050565b6000611a6f601083611e11565b9150611a7a826121b2565b602082019050919050565b6000611a92602883611e11565b9150611a9d826121db565b604082019050919050565b6000611ab5602083611e11565b9150611ac08261222a565b602082019050919050565b6000611ad8602183611e11565b9150611ae382612253565b604082019050919050565b6000611afb602583611e11565b9150611b06826122a2565b604082019050919050565b6000611b1e602483611e11565b9150611b29826122f1565b604082019050919050565b6000611b41602583611e11565b9150611b4c82612340565b604082019050919050565b6000611b64601f83611e11565b9150611b6f8261238f565b602082019050919050565b611b8381611eea565b82525050565b611b9281611ef4565b82525050565b6000602082019050611bad6000830184611916565b92915050565b6000602082019050611bc86000830184611925565b92915050565b60006020820190508181036000830152611be88184611934565b905092915050565b60006020820190508181036000830152611c098161196d565b9050919050565b60006020820190508181036000830152611c2981611990565b9050919050565b60006020820190508181036000830152611c49816119b3565b9050919050565b60006020820190508181036000830152611c69816119d6565b9050919050565b60006020820190508181036000830152611c89816119f9565b9050919050565b60006020820190508181036000830152611ca981611a1c565b9050919050565b60006020820190508181036000830152611cc981611a3f565b9050919050565b60006020820190508181036000830152611ce981611a62565b9050919050565b60006020820190508181036000830152611d0981611a85565b9050919050565b60006020820190508181036000830152611d2981611aa8565b9050919050565b60006020820190508181036000830152611d4981611acb565b9050919050565b60006020820190508181036000830152611d6981611aee565b9050919050565b60006020820190508181036000830152611d8981611b11565b9050919050565b60006020820190508181036000830152611da981611b34565b9050919050565b60006020820190508181036000830152611dc981611b57565b9050919050565b6000602082019050611de56000830184611b7a565b92915050565b6000602082019050611e006000830184611b89565b92915050565b600081519050919050565b600082825260208201905092915050565b6000611e2d82611eea565b9150611e3883611eea565b9250827fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff03821115611e6d57611e6c611f66565b5b828201905092915050565b6000611e8382611eea565b9150611e8e83611eea565b925082821015611ea157611ea0611f66565b5b828203905092915050565b6000611eb782611eca565b9050919050565b60008115159050919050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b6000819050919050565b600060ff82169050919050565b60005b83811015611f1f578082015181840152602081019050611f04565b83811115611f2e576000848401525b50505050565b60006002820490506001821680611f4c57607f821691505b60208210811415611f6057611f5f611f95565b5b50919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fd5b7f4e487b7100000000000000000000000000000000000000000000000000000000600052602260045260246000fd5b6000601f19601f8301169050919050565b7f45524332303a207472616e7366657220746f20746865207a65726f206164647260008201527f6573730000000000000000000000000000000000000000000000000000000000602082015250565b7f5061757361626c653a206e6f7420706175736564000000000000000000000000600082015250565b7f45524332303a206275726e20616d6f756e7420657863656564732062616c616e60008201527f6365000000000000000000000000000000000000000000000000000000000000602082015250565b7f4f776e61626c653a206e6577206f776e657220697320746865207a65726f206160008201527f6464726573730000000000000000000000000000000000000000000000000000602082015250565b7f45524332303a20617070726f766520746f20746865207a65726f20616464726560008201527f7373000000000000000000000000000000000000000000000000000000000000602082015250565b7f45524332303a207472616e7366657220616d6f756e742065786365656473206260008201527f616c616e63650000000000000000000000000000000000000000000000000000602082015250565b7f5455533a204f6e6c79206d696e74657200000000000000000000000000000000600082015250565b7f5061757361626c653a2070617573656400000000000000000000000000000000600082015250565b7f45524332303a207472616e7366657220616d6f756e742065786365656473206160008201527f6c6c6f77616e6365000000000000000000000000000000000000000000000000602082015250565b7f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e6572600082015250565b7f45524332303a206275726e2066726f6d20746865207a65726f2061646472657360008201527f7300000000000000000000000000000000000000000000000000000000000000602082015250565b7f45524332303a207472616e736665722066726f6d20746865207a65726f20616460008201527f6472657373000000000000000000000000000000000000000000000000000000602082015250565b7f45524332303a20617070726f76652066726f6d20746865207a65726f2061646460008201527f7265737300000000000000000000000000000000000000000000000000000000602082015250565b7f45524332303a2064656372656173656420616c6c6f77616e63652062656c6f7760008201527f207a65726f000000000000000000000000000000000000000000000000000000602082015250565b7f45524332303a206d696e7420746f20746865207a65726f206164647265737300600082015250565b6123c181611eac565b81146123cc57600080fd5b50565b6123d881611ebe565b81146123e357600080fd5b50565b6123ef81611eea565b81146123fa57600080fd5b5056fea2646970667358221220a42f79643656a83c09c3f4bef7d01d6078a62efc147eb7142f1bba28b25073a864736f6c63430008030033";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_DECIMALS = "decimals";

    public static final String FUNC_VERSION = "version";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_SYMBOL = "symbol";

    public static final String FUNC_TRANSFER = "transfer";

    public static final String FUNC_APPROVEANDCALL = "approveAndCall";

    public static final String FUNC_ALLOWANCE = "allowance";

    public static final Event TRANSFER_EVENT = new Event("Transfer",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event APPROVAL_EVENT = new Event("Approval",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected Erc20Wrapper(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Erc20Wrapper(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Erc20Wrapper(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Erc20Wrapper(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<String> name() {
        final Function function = new Function(FUNC_NAME,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> approve(String _spender, BigInteger _value) {
        final Function function = new Function(
                FUNC_APPROVE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_spender),
                        new org.web3j.abi.datatypes.generated.Uint256(_value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> totalSupply() {
        final Function function = new Function(FUNC_TOTALSUPPLY,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> transferFrom(String _from, String _to, BigInteger _value) {
        final Function function = new Function(
                FUNC_TRANSFERFROM,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_from),
                        new org.web3j.abi.datatypes.Address(_to),
                        new org.web3j.abi.datatypes.generated.Uint256(_value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> decimals() {
        final Function function = new Function(FUNC_DECIMALS,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> version() {
        final Function function = new Function(FUNC_VERSION,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> balanceOf(String _owner) {
        final Function function = new Function(FUNC_BALANCEOF,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_owner)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> symbol() {
        final Function function = new Function(FUNC_SYMBOL,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> transfer(String _to, BigInteger _value) {
        final Function function = new Function(
                FUNC_TRANSFER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_to),
                        new org.web3j.abi.datatypes.generated.Uint256(_value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> approveAndCall(String _spender, BigInteger _value, byte[] _extraData) {
        final Function function = new Function(
                FUNC_APPROVEANDCALL,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_spender),
                        new org.web3j.abi.datatypes.generated.Uint256(_value),
                        new org.web3j.abi.datatypes.DynamicBytes(_extraData)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> allowance(String _owner, String _spender) {
        final Function function = new Function(FUNC_ALLOWANCE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_owner),
                        new org.web3j.abi.datatypes.Address(_spender)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt);
        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<TransferEventResponse> transferEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, TransferEventResponse>() {
            @Override
            public TransferEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSFER_EVENT, log);
                TransferEventResponse typedResponse = new TransferEventResponse();
                typedResponse.log = log;
                typedResponse._from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._to = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<TransferEventResponse> transferEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT));
        return transferEventFlowable(filter);
    }

    public List<ApprovalEventResponse> getApprovalEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(APPROVAL_EVENT, transactionReceipt);
        ArrayList<ApprovalEventResponse> responses = new ArrayList<ApprovalEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ApprovalEventResponse typedResponse = new ApprovalEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._spender = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ApprovalEventResponse> approvalEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, ApprovalEventResponse>() {
            @Override
            public ApprovalEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(APPROVAL_EVENT, log);
                ApprovalEventResponse typedResponse = new ApprovalEventResponse();
                typedResponse.log = log;
                typedResponse._owner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._spender = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ApprovalEventResponse> approvalEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVAL_EVENT));
        return approvalEventFlowable(filter);
    }

    @Deprecated
    public static Erc20Wrapper load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Erc20Wrapper(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Erc20Wrapper load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Erc20Wrapper(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Erc20Wrapper load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Erc20Wrapper(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Erc20Wrapper load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Erc20Wrapper(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Erc20Wrapper> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, BigInteger _initialAmount, String _tokenName, BigInteger _decimalUnits, String _tokenSymbol) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_initialAmount),
                new org.web3j.abi.datatypes.Utf8String(_tokenName),
                new org.web3j.abi.datatypes.generated.Uint8(_decimalUnits),
                new org.web3j.abi.datatypes.Utf8String(_tokenSymbol)));
        return deployRemoteCall(Erc20Wrapper.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<Erc20Wrapper> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger _initialAmount, String _tokenName, BigInteger _decimalUnits, String _tokenSymbol) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_initialAmount),
                new org.web3j.abi.datatypes.Utf8String(_tokenName),
                new org.web3j.abi.datatypes.generated.Uint8(_decimalUnits),
                new org.web3j.abi.datatypes.Utf8String(_tokenSymbol)));
        return deployRemoteCall(Erc20Wrapper.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Erc20Wrapper> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger _initialAmount, String _tokenName, BigInteger _decimalUnits, String _tokenSymbol) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_initialAmount),
                new org.web3j.abi.datatypes.Utf8String(_tokenName),
                new org.web3j.abi.datatypes.generated.Uint8(_decimalUnits),
                new org.web3j.abi.datatypes.Utf8String(_tokenSymbol)));
        return deployRemoteCall(Erc20Wrapper.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Erc20Wrapper> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger _initialAmount, String _tokenName, BigInteger _decimalUnits, String _tokenSymbol) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_initialAmount),
                new org.web3j.abi.datatypes.Utf8String(_tokenName),
                new org.web3j.abi.datatypes.generated.Uint8(_decimalUnits),
                new org.web3j.abi.datatypes.Utf8String(_tokenSymbol)));
        return deployRemoteCall(Erc20Wrapper.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class TransferEventResponse {
        public Log log;

        public String _from;

        public String _to;

        public BigInteger _value;
    }

    public static class ApprovalEventResponse {
        public Log log;

        public String _owner;

        public String _spender;

        public BigInteger _value;
    }
}
